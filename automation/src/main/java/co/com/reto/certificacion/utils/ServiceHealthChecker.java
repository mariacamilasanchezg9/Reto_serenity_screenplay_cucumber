package co.com.reto.certificacion.utils;

import co.com.reto.certificacion.exceptions.ServiceNotAvailableException;
import io.restassured.RestAssured;

/**
 * Verificador de disponibilidad del microservicio bajo pruebas.
 *
 * <p><b>Que hace:</b> consulta {@code GET /api/auth/health} y tambien expone el reinicio del
 * estado del servicio mediante {@code POST /api/test-support/reset}.</p>
 *
 * <p><b>Para que se usa:</b> lo invocan los hooks antes de cada escenario. Asi se consigue:</p>
 * <ul>
 *   <li>Fallar de inmediato y con un mensaje entendible si el servicio no esta levantado.</li>
 *   <li>Garantizar que cada escenario arranca con la cuenta desbloqueada e independiente.</li>
 * </ul>
 */
public final class ServiceHealthChecker {

    /** Clase de utilidad: no se instancia. */
    private ServiceHealthChecker() {
        throw new UnsupportedOperationException("Clase de utilidad, no instanciable");
    }

    /**
     * Comprueba que el microservicio responda.
     *
     * @throws ServiceNotAvailableException si el servicio no responde o devuelve un codigo distinto de 200.
     */
    public static void verifyServiceIsUp() {
        try {
            int statusCode = RestAssured.given()
                    .baseUri(Environment.apiBaseUrl())
                    .when()
                    .get(ApiEndpoints.HEALTH)
                    .getStatusCode();

            if (statusCode != 200) {
                throw new ServiceNotAvailableException(serviceDownMessage(" respondio HTTP " + statusCode), null);
            }
        } catch (ServiceNotAvailableException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceNotAvailableException(serviceDownMessage(" no responde"), e);
        }
    }

    /**
     * Devuelve el microservicio a su estado inicial: cuentas desbloqueadas y contadores en cero.
     *
     * <p>Se ejecuta antes de cada escenario para que el caso de bloqueo no afecte a los demas.</p>
     */
    public static void resetServiceState() {
        RestAssured.given()
                .baseUri(Environment.apiBaseUrl())
                .when()
                .post(ApiEndpoints.RESET)
                .then()
                .statusCode(200);
    }

    /**
     * Construye el mensaje de ayuda que se muestra cuando el servicio no esta disponible.
     *
     * @param detail detalle tecnico del fallo detectado.
     * @return mensaje con la causa y el comando para levantar el servicio.
     */
    private static String serviceDownMessage(String detail) {
        return "El microservicio de autenticacion en " + Environment.apiBaseUrl() + detail + "."
                + System.lineSeparator()
                + "Levantelo antes de ejecutar las pruebas con: gradlew :auth-service:bootRun";
    }
}
