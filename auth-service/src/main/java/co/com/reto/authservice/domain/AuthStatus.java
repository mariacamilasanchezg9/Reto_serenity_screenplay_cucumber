package co.com.reto.authservice.domain;

/**
 * Resultado posible de un intento de autenticacion.
 *
 * <p><b>Para que se usa:</b> es el contrato unico que comparten la capa web y la capa API.
 * Las pruebas automatizadas validan este valor en el campo {@code status} de la respuesta JSON
 * y en el mensaje mostrado en pantalla.</p>
 */
public enum AuthStatus {

    /** Usuario y contrasena correctos: se entrega un token de sesion. */
    SUCCESS,

    /** Credenciales invalidas. La cuenta aun admite mas intentos. */
    INVALID_CREDENTIALS,

    /** La cuenta quedo bloqueada por superar el maximo de intentos fallidos permitidos. */
    ACCOUNT_LOCKED
}
