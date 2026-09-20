package co.com.reto.certificacion.models;

/**
 * Credenciales de acceso usadas tanto por la automatizacion web como por la de API.
 *
 * <p><b>Para que se usa:</b> evita pasar cadenas sueltas entre capas. Un unico objeto viaja desde
 * el step definition hasta la Task, lo que hace los metodos mas legibles y a prueba de errores por
 * invertir el orden de los parametros.</p>
 *
 * @param username nombre de usuario.
 * @param password contrasena.
 */
public record Credentials(String username, String password) {

    /**
     * Fabrica legible para construir credenciales.
     *
     * @param username nombre de usuario.
     * @param password contrasena.
     * @return la instancia de credenciales.
     */
    public static Credentials of(String username, String password) {
        return new Credentials(username, password);
    }

    /**
     * Convierte las credenciales al cuerpo JSON que espera {@code POST /api/auth/login}.
     *
     * <p>Se construye a mano (sin librerias de serializacion) para que el payload enviado sea
     * explicito y facil de leer en el reporte de Serenity.</p>
     *
     * @return el JSON con los campos {@code username} y {@code password}.
     */
    public String asJsonBody() {
        return String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
    }
}
