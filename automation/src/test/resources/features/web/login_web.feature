# language: es
@web
Característica: Autenticación de usuarios por interfaz web
  Como usuario registrado del portal
  Quiero iniciar sesión con mis credenciales
  Para acceder al sistema de forma segura

  Antecedentes:
    Dado que el usuario se encuentra en la página de inicio de sesión

  @web @login @exitoso
  Escenario: Ingreso exitoso con la contraseña correcta
    Cuando intenta iniciar sesión con el usuario "usuario.demo" y la contraseña "Clave123*"
    Entonces debería visualizar el mensaje de bienvenida "Bienvenido al sistema"
    Y el usuario autenticado debería ser "usuario.demo"

  @web @login @credenciales-invalidas
  Escenario: Ingreso rechazado con la contraseña incorrecta
    Cuando intenta iniciar sesión con el usuario "usuario.demo" y la contraseña "ClaveErrada1"
    Entonces debería visualizar el mensaje de error "Usuario o contrasena incorrectos"
    Y debería ver que le quedan 2 intentos disponibles

  @web @login @bloqueo
  Escenario: Bloqueo de la cuenta tras agotar los intentos fallidos
    Cuando intenta iniciar sesión 3 veces con el usuario "usuario.demo" y la contraseña "ClaveErrada1"
    Entonces debería visualizar el mensaje de bloqueo "La cuenta ha sido bloqueada por superar 3 intentos fallidos"
    Cuando intenta iniciar sesión con el usuario "usuario.demo" y la contraseña "Clave123*"
    Entonces debería visualizar el mensaje de bloqueo "La cuenta ha sido bloqueada por superar 3 intentos fallidos"
