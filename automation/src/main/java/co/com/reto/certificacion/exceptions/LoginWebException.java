package co.com.reto.certificacion.exceptions;

/**
 * Error de validacion en los escenarios de login por interfaz web.
 *
 * <p><b>Para que se usa:</b> es el mensaje que Serenity muestra en el reporte cuando una asercion
 * de la capa web no se cumple (por ejemplo, el mensaje de bloqueo no aparecio). Extiende
 * {@link AssertionError} para que Cucumber marque el escenario como <i>fallido</i> y no como
 * <i>roto</i> por un error tecnico.</p>
 */
public class LoginWebException extends AssertionError {

    /**
     * @param message descripcion del comportamiento esperado frente al obtenido.
     */
    public LoginWebException(String message) {
        super(message);
    }
}
