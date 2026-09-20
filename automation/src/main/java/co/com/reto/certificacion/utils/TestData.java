package co.com.reto.certificacion.utils;

import co.com.reto.certificacion.models.Credentials;

/**
 * Datos de prueba conocidos del microservicio.
 *
 * <p><b>Para que se usa:</b> los escenarios Gherkin envian sus propios datos, pero los hooks y
 * las utilidades necesitan un usuario valido de referencia. Tenerlo aqui evita duplicar literales
 * a lo largo del proyecto.</p>
 */
public final class TestData {

    /** Usuario precargado en el microservicio. */
    public static final String VALID_USERNAME = "usuario.demo";

    /** Contrasena correcta del usuario precargado. */
    public static final String VALID_PASSWORD = "Clave123*";

    /** Numero de intentos fallidos que provocan el bloqueo de la cuenta. */
    public static final int MAX_FAILED_ATTEMPTS = 3;

    /** Clase de utilidad: no se instancia. */
    private TestData() {
        throw new UnsupportedOperationException("Clase de utilidad, no instanciable");
    }

    /**
     * @return las credenciales validas del usuario de demostracion.
     */
    public static Credentials validCredentials() {
        return Credentials.of(VALID_USERNAME, VALID_PASSWORD);
    }
}
