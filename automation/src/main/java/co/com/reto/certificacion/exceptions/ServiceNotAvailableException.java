package co.com.reto.certificacion.exceptions;

/**
 * Se lanza cuando el microservicio bajo pruebas no responde.
 *
 * <p><b>Para que se usa:</b> convierte un fallo de infraestructura (servicio apagado) en un
 * mensaje claro y accionable, en lugar de dejar que las pruebas fallen con un
 * {@code ConnectException} confuso o con un "elemento no encontrado" del navegador.</p>
 */
public class ServiceNotAvailableException extends AssertionError {

    /**
     * @param message detalle del problema y la accion sugerida para resolverlo.
     * @param cause   excepcion tecnica original.
     */
    public ServiceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
