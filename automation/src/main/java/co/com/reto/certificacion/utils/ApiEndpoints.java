package co.com.reto.certificacion.utils;

/**
 * Catalogo de rutas de la API del microservicio.
 *
 * <p><b>Para que se usa:</b> centraliza los paths para que ninguna Task, Interaction o Hook
 * escriba una ruta como texto libre. Si el contrato cambia, se ajusta en un solo lugar.</p>
 */
public final class ApiEndpoints {

    /** Autenticacion de usuario. Metodo POST, cuerpo JSON con usuario y contrasena. */
    public static final String LOGIN = "/api/auth/login";

    /** Verificacion de disponibilidad del microservicio. Metodo GET. */
    public static final String HEALTH = "/api/auth/health";

    /** Soporte de pruebas: desbloquea cuentas y reinicia contadores. Metodo POST. */
    public static final String RESET = "/api/test-support/reset";

    /** Clase de utilidad: no se instancia. */
    private ApiEndpoints() {
        throw new UnsupportedOperationException("Clase de utilidad, no instanciable");
    }
}
