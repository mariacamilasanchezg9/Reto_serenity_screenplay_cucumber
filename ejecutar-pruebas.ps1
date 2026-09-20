<#
.SYNOPSIS
    Ejecuta la automatizacion completa con un solo comando.

.DESCRIPTION
    Este script hace todo el trabajo manual por usted:
      1. Compila y empaqueta el microservicio de autenticacion.
      2. Lo levanta en segundo plano y espera a que responda.
      3. Ejecuta la suite de pruebas solicitada (web, api o ambas).
      4. Apaga el microservicio y abre el reporte de Serenity.

    El apagado del microservicio esta dentro de un bloque 'finally', por lo que se
    ejecuta incluso si las pruebas fallan o si usted interrumpe el script.

.PARAMETER Suite
    Que se ejecuta: 'todas' (por defecto), 'web' o 'api'.

.PARAMETER Headless
    Ejecuta el navegador sin ventana visible. Util para servidores de integracion continua.

.EXAMPLE
    .\ejecutar-pruebas.ps1
    .\ejecutar-pruebas.ps1 -Suite api
    .\ejecutar-pruebas.ps1 -Suite web -Headless
#>
param(
    [ValidateSet('todas', 'web', 'api')]
    [string]$Suite = 'todas',
    [switch]$Headless
)

$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot

# Marca unica que identifica al proceso java levantado por este script.
$JarRelativo = 'auth-service\build\libs\auth-service.jar'
$UrlSalud    = 'http://localhost:8080/api/auth/health'

<#
    Detiene cualquier instancia del microservicio que este corriendo.
    Se busca por la linea de comandos y no solo por el identificador de proceso,
    porque asi se limpia tambien una instancia que haya quedado de una ejecucion previa.
#>
function Stop-Microservicio {
    Get-CimInstance Win32_Process -Filter "Name='java.exe'" |
        Where-Object { $_.CommandLine -like '*auth-service.jar*' } |
        ForEach-Object {
            Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue
        }
}

# --- 1. Empaquetar el microservicio -----------------------------------------
Write-Host '[1/4] Compilando el microservicio de autenticacion...' -ForegroundColor Cyan
& .\gradlew.bat ':auth-service:bootJar' --console=plain
if ($LASTEXITCODE -ne 0) { throw 'Fallo la compilacion del microservicio' }

# Si quedo una instancia de una corrida anterior, se apaga para liberar el puerto 8080.
Stop-Microservicio

$resultado = 1
try {
    # --- 2. Levantarlo y esperar a que este disponible -----------------------
    Write-Host '[2/4] Levantando el microservicio en http://localhost:8080 ...' -ForegroundColor Cyan
    Start-Process -FilePath 'java' -ArgumentList '-jar', $JarRelativo -WindowStyle Hidden | Out-Null

    $arriba = $false
    foreach ($intento in 1..30) {
        Start-Sleep -Seconds 1
        try {
            Invoke-RestMethod -Uri $UrlSalud -TimeoutSec 2 | Out-Null
            $arriba = $true
            break
        } catch { }
    }
    if (-not $arriba) { throw 'El microservicio no respondio despues de 30 segundos' }
    Write-Host '      Microservicio arriba.' -ForegroundColor Green

    # --- 3. Ejecutar la suite solicitada -------------------------------------
    $tarea = switch ($Suite) {
        'web'   { ':automation:webTests' }
        'api'   { ':automation:apiTests' }
        default { ':automation:test' }
    }
    $argumentos = @($tarea, '--console=plain')
    if ($Headless) { $argumentos += '-Dheadless.mode=true' }

    Write-Host "[3/4] Ejecutando la suite '$Suite'..." -ForegroundColor Cyan
    & .\gradlew.bat @argumentos
    $resultado = $LASTEXITCODE
}
finally {
    # --- 4. Apagar el microservicio pase lo que pase -------------------------
    Write-Host '[4/4] Apagando el microservicio...' -ForegroundColor Cyan
    Stop-Microservicio
}

$reporte = Join-Path $PSScriptRoot 'automation\target\site\serenity\index.html'
if (Test-Path $reporte) {
    Write-Host "Reporte de Serenity: $reporte" -ForegroundColor Green
    Start-Process $reporte
}

exit $resultado
