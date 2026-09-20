# Reto de Automatización — Serenity BDD + Screenplay + Cucumber

Automatización **web** y de **API REST** de un flujo de autenticación, cubriendo tres casos de prueba:

| # | Caso de prueba | Web | API |
|---|----------------|-----|-----|
| 1 | Contraseña **correcta** → ingreso exitoso | Sí | Sí |
| 2 | Contraseña **incorrecta** → ingreso rechazado | Sí | Sí |
| 3 | **Bloqueo** de la cuenta tras 3 intentos fallidos | Sí | Sí |

---

## 1. ¿Por qué hay un microservicio en el proyecto?

El reto pedía usar una página pública si existía alguna que sirviera. Se revisaron las opciones
habituales (*the-internet*, *SauceDemo*, *PracticeTestAutomation*) y **ninguna implementa bloqueo de
cuenta por intentos fallidos de forma determinística**, que es justamente el tercer caso de prueba.

Por eso el proyecto incluye su propio **microservicio Spring Boot** (`auth-service`) que expone:

- una **página web de login** (`/login`) para automatizar con Selenium, y
- una **API REST** (`POST /api/auth/login`) para automatizar con SerenityRest,

ambas sobre **la misma regla de negocio**. Así los tres casos se comportan igual en los dos canales,
las pruebas son 100 % reproducibles y no dependen de internet ni de un sitio de terceros.

---

## 2. Requisitos

| Herramienta | Versión | Verificación |
|-------------|---------|--------------|
| **Java JDK** | 21 (probado con 21.0.7) | `java -version` |
| **Gradle** | No hace falta instalarlo: el proyecto incluye el *wrapper* | `gradlew -version` |
| **Google Chrome** | Cualquier versión reciente | Solo para la suite web |
| **IntelliJ IDEA** | Community o Ultimate | — |

> El `chromedriver` **no se instala a mano**: Selenium Manager lo descarga automáticamente.

---

## 3. Ejecución rápida (un solo comando)

Desde la raíz del proyecto, en **PowerShell**:

```powershell
# Todo: levanta el microservicio, corre web + API, lo apaga y abre el reporte
.\ejecutar-pruebas.ps1

# Solo la suite de API
.\ejecutar-pruebas.ps1 -Suite api

# Solo la suite web, sin ventana de navegador
.\ejecutar-pruebas.ps1 -Suite web -Headless
```

El script se encarga de compilar, levantar el servicio, esperar a que responda, ejecutar las
pruebas, apagarlo y abrir el reporte de Serenity.

> Si PowerShell bloquea el script, ejecute una sola vez:
> `Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass`

---

## 4. Ejecución manual (dos terminales)

**Terminal 1 — levantar el microservicio** (déjelo corriendo):

```powershell
.\gradlew :auth-service:bootRun
```

Espere a ver `Started AuthServiceApplication`. Puede abrir <http://localhost:8080/login> en el
navegador para ver la pantalla que se va a automatizar.

**Terminal 2 — ejecutar las pruebas:**

```powershell
.\gradlew :automation:test        # web + API
.\gradlew :automation:webTests    # solo web
.\gradlew :automation:apiTests    # solo API
```

Opciones útiles:

```powershell
.\gradlew :automation:webTests -Dheadless.mode=true   # navegador sin ventana
.\gradlew :automation:test -Denvironment=qa           # apuntar a otro ambiente de serenity.conf
```

---

## 5. Ejecución desde IntelliJ IDEA

1. **Abrir el proyecto**: `File > Open` y seleccione la carpeta raíz. IntelliJ detecta Gradle e
   importa los dos módulos (`auth-service` y `automation`).
2. **Configurar el JDK**: `File > Project Structure > Project SDK` y elija **21**.
3. **Levantar el microservicio**: abra
   `auth-service/src/main/java/co/com/reto/authservice/AuthServiceApplication.java`
   y presione el botón de ejecutar junto a la clase (`Ctrl+Shift+F10`). Déjelo corriendo.
4. **Ejecutar las pruebas**: abra el runner que desee y presione ejecutar:
   - `automation/src/test/java/co/com/reto/certificacion/runners/WebLoginRunner.java`
   - `automation/src/test/java/co/com/reto/certificacion/runners/ApiLoginRunner.java`
5. **Ejecutar un solo escenario**: abra el archivo `.feature` y presione ejecutar junto al escenario
   (requiere el plugin *Cucumber for Java*, incluido en IntelliJ Ultimate y disponible gratis en
   Community desde `Settings > Plugins`).

---

## 6. Reporte de resultados

Al terminar cualquier ejecución se genera el reporte HTML de Serenity:

```
automation/target/site/serenity/index.html
```

Ábralo con doble clic. Incluye los escenarios, cada paso ejecutado por el actor, las peticiones y
respuestas HTTP de la suite API y capturas de pantalla de los pasos fallidos en la suite web.

---

## 7. Estructura del proyecto

```
Reto_serenity_screenplay_cucumber/
│
├── ejecutar-pruebas.ps1            <- script de ejecución de un solo comando
├── settings.gradle                 <- declara los dos módulos
├── build.gradle                    <- configuración común (Java 21, repositorios)
├── gradle.properties               <- versiones centralizadas
│
├── auth-service/                   <- MICROSERVICIO (sistema bajo pruebas)
│   └── src/main/
│       ├── java/co/com/reto/authservice/
│       │   ├── controller/         <- LoginWebController (web), AuthApiController (REST),
│       │   │                          TestSupportController (reinicio de estado)
│       │   ├── service/            <- AuthenticationService: regla de negocio y bloqueo
│       │   ├── domain/             <- UserAccount, AuthStatus
│       │   └── model/              <- LoginRequest, LoginResponse
│       └── resources/
│           ├── templates/          <- login.html, home.html
│           └── application.yml     <- puerto e intentos máximos permitidos
│
└── automation/                     <- AUTOMATIZACIÓN
    └── src/
        ├── main/java/co/com/reto/certificacion/
        │   ├── tasks/              <- Login, AttemptLogin, Authenticate, AttemptAuthentication
        │   ├── questions/
        │   │   ├── web/            <- WelcomeMessage, ErrorMessage, LockMessage, ...
        │   │   └── api/            <- ResponseStatusCode, ResponseField, SessionToken, ...
        │   ├── interactions/       <- EnterCredentials, SendLoginRequest, NavigateTo
        │   ├── userinterfaces/     <- LoginPage, HomePage (localizadores)
        │   ├── exceptions/         <- LoginWebException, LoginApiException, ServiceNotAvailable...
        │   ├── utils/              <- Environment, ApiEndpoints, TestData, ServiceHealthChecker
        │   └── models/             <- Credentials
        │
        └── test/
            ├── java/co/com/reto/certificacion/
            │   ├── runners/         <- WebLoginRunner, ApiLoginRunner
            │   ├── stepdefinitions/ <- LoginWebStepDefinitions, LoginApiStepDefinitions
            │   └── hooks/           <- WebHooks, ApiHooks
            └── resources/
                ├── features/web/   <- login_web.feature
                ├── features/api/   <- login_api.feature
                └── serenity.conf   <- navegador, URLs y ambientes
```

### Para qué sirve cada capa de Screenplay

| Capa | Responsabilidad | Regla práctica |
|------|-----------------|----------------|
| **userinterfaces** | Localizadores de los elementos de pantalla | Es el **único** lugar con selectores CSS/XPath |
| **interactions** | Acción atómica sobre un elemento o petición HTTP | "Escribir", "hacer clic", "enviar POST" |
| **tasks** | Intención de negocio compuesta por interacciones | "Iniciar sesión", "intentar 3 veces" |
| **questions** | Leer el estado del sistema para poder afirmar sobre él | No modifican nada, solo consultan |
| **exceptions** | Errores de negocio con mensajes entendibles | Reemplazan trazas técnicas confusas |
| **utils** | Configuración, endpoints, datos y verificaciones de apoyo | Evita valores "quemados" en el código |
| **models** | Objetos de datos que viajan entre capas | `Credentials` en lugar de cadenas sueltas |
| **stepdefinitions** | Traducen el texto Gherkin a Tasks y Questions | Solo orquestan; no contienen lógica |
| **hooks** | Preparan y limpian el ambiente de cada escenario | Reinician el estado del servicio |
| **runners** | Indican a JUnit 5 qué escenarios ejecutar | Uno por suite: web y API |

---

## 8. Datos de prueba

| Dato | Valor |
|------|-------|
| Usuario válido | `usuario.demo` |
| Contraseña válida | `Clave123*` |
| Contraseña inválida usada en los escenarios | `ClaveErrada1` |
| Intentos fallidos permitidos antes del bloqueo | `3` |

Para cambiar el número de intentos permitidos, edite `auth.max-failed-attempts` en
`auth-service/src/main/resources/application.yml`.

---

## 9. Contrato de la API

### `POST /api/auth/login`

Petición:

```json
{ "username": "usuario.demo", "password": "Clave123*" }
```

Respuestas:

| Caso | HTTP | Campo `status` | `token` | `remainingAttempts` |
|------|------|----------------|---------|---------------------|
| Contraseña correcta | `200` | `SUCCESS` | UUID de sesión | `3` |
| Contraseña incorrecta | `401` | `INVALID_CREDENTIALS` | `null` | `2`, luego `1` |
| Cuenta bloqueada | `423` | `ACCOUNT_LOCKED` | `null` | `0` |

Una vez bloqueada, la cuenta responde `423` **incluso con la contraseña correcta**: así lo valida
el tercer escenario de cada suite.

### Endpoints de apoyo

| Endpoint | Método | Para qué sirve |
|----------|--------|----------------|
| `/api/auth/health` | `GET` | Los hooks verifican que el servicio esté arriba antes de cada escenario |
| `/api/test-support/reset` | `POST` | Desbloquea las cuentas entre escenarios para que sean independientes |

---

## 10. Documentación Javadoc

Todo el código está documentado explicando **qué hace** y **para qué se usa** cada clase. Para
generar la documentación navegable en HTML:

```powershell
.\gradlew :automation:javadoc :auth-service:javadoc
```

Resultado:

- `automation/build/docs/javadoc/index.html`
- `auth-service/build/docs/javadoc/index.html`

---

## 11. Problemas frecuentes

| Síntoma | Causa y solución |
|---------|------------------|
| `El microservicio ... no responde` | El servicio no está levantado. Ejecute `.\gradlew :auth-service:bootRun` o use `.\ejecutar-pruebas.ps1` |
| `Port 8080 was already in use` | Otro proceso usa el puerto. Ciérrelo, o cambie `server.port` en `application.yml` y las URLs en `serenity.conf` |
| La suite web falla al abrir Chrome | Actualice Google Chrome; Selenium Manager descargará el driver correspondiente |
| Quiere ejecutar sin ver el navegador | Agregue `-Dheadless.mode=true` |
