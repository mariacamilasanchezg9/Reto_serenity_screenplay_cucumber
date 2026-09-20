package co.com.reto.certificacion.exceptions;

/**
 * Error de validacion en los escenarios de login por API REST.
 *
 * <p><b>Para que se usa:</b> agrupa los fallos de asercion sobre la respuesta HTTP (codigo de
 * estado, status de negocio, token o intentos restantes) bajo un tipo propio, facil de identificar
 * en el reporte de Serenity.</p>
 */
public class LoginApiException extends AssertionError {

    /**
     * @param message descripcion del comportamiento esperado frente al obtenido.
     */
    public LoginApiException(String message) {
        super(message);
    }
}
