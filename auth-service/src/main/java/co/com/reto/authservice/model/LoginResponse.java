package co.com.reto.authservice.model;

import co.com.reto.authservice.domain.AuthStatus;

/**
 * Payload JSON de salida del endpoint {@code POST /api/auth/login}.
 *
 * <p><b>Para que se usa:</b> es el objeto que las pruebas de API interrogan con SerenityRest.
 * Siempre viaja con los mismos campos (aunque alguno sea {@code null}) para que las aserciones
 * sean estables y no dependan de la rama de negocio ejecutada.</p>
 *
 * @param status             resultado del intento: SUCCESS, INVALID_CREDENTIALS o ACCOUNT_LOCKED.
 * @param message            mensaje legible para el usuario final.
 * @param username           usuario sobre el que se intento autenticar.
 * @param token              token de sesion; solo viene informado cuando el status es SUCCESS.
 * @param remainingAttempts  intentos que restan antes de que la cuenta quede bloqueada.
 */
public record LoginResponse(
        AuthStatus status,
        String message,
        String username,
        String token,
        int remainingAttempts) {
}
