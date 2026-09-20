# language: es
@api
Característica: Autenticación de usuarios por API REST
  Como sistema consumidor del servicio de autenticación
  Quiero enviar credenciales al endpoint POST /api/auth/login
  Para obtener un token de sesión o un rechazo controlado

  @api @login @exitoso
  Escenario: Autenticación exitosa con la contraseña correcta
    Cuando envía la petición de autenticación con el usuario "usuario.demo" y la contraseña "Clave123*"
    Entonces el código de respuesta debería ser 200
    Y el estado de la autenticación debería ser "SUCCESS"
    Y debería recibir un token de sesión válido

  @api @login @credenciales-invalidas
  Escenario: Autenticación rechazada con la contraseña incorrecta
    Cuando envía la petición de autenticación con el usuario "usuario.demo" y la contraseña "ClaveErrada1"
    Entonces el código de respuesta debería ser 401
    Y el estado de la autenticación debería ser "INVALID_CREDENTIALS"
    Y los intentos restantes deberían ser 2
    Y no debería recibir ningún token de sesión

  @api @login @bloqueo
  Escenario: Bloqueo de la cuenta tras agotar los intentos fallidos
    Cuando envía 3 peticiones de autenticación con el usuario "usuario.demo" y la contraseña "ClaveErrada1"
    Entonces el código de respuesta debería ser 423
    Y el estado de la autenticación debería ser "ACCOUNT_LOCKED"
    Y los intentos restantes deberían ser 0
    Cuando envía la petición de autenticación con el usuario "usuario.demo" y la contraseña "Clave123*"
    Entonces el código de respuesta debería ser 423
    Y el estado de la autenticación debería ser "ACCOUNT_LOCKED"
