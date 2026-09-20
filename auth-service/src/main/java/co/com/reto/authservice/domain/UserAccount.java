package co.com.reto.authservice.domain;

/**
 * Cuenta de usuario en memoria con el control de intentos fallidos.
 *
 * <p><b>Para que se usa:</b> modela la regla de negocio de bloqueo. Cada vez que alguien falla la
 * contrasena se incrementa un contador; al alcanzar el maximo permitido la cuenta se marca como
 * bloqueada y ya no acepta ni siquiera la contrasena correcta.</p>
 *
 * <p>La informacion vive solo en memoria (no hay base de datos) para que el entorno de pruebas
 * sea liviano, reproducible y facil de reiniciar entre escenarios.</p>
 */
public class UserAccount {

    private final String username;
    private final String password;
    private int failedAttempts;
    private boolean locked;

    /**
     * Crea una cuenta desbloqueada y sin intentos fallidos.
     *
     * @param username nombre de usuario.
     * @param password contrasena valida de la cuenta.
     */
    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.failedAttempts = 0;
        this.locked = false;
    }

    /** @return el nombre de usuario de la cuenta. */
    public String getUsername() {
        return username;
    }

    /**
     * Verifica si la contrasena recibida corresponde a la de la cuenta.
     *
     * @param candidate contrasena enviada por el cliente.
     * @return {@code true} cuando coincide exactamente.
     */
    public boolean matchesPassword(String candidate) {
        return password.equals(candidate);
    }

    /** @return {@code true} si la cuenta esta bloqueada y no admite autenticacion. */
    public boolean isLocked() {
        return locked;
    }

    /** @return cantidad de intentos fallidos acumulados desde el ultimo reinicio. */
    public int getFailedAttempts() {
        return failedAttempts;
    }

    /**
     * Registra un intento fallido y bloquea la cuenta si se alcanza el maximo permitido.
     *
     * @param maxAttempts numero de intentos fallidos tolerados antes del bloqueo.
     */
    public void registerFailedAttempt(int maxAttempts) {
        failedAttempts++;
        if (failedAttempts >= maxAttempts) {
            locked = true;
        }
    }

    /** Reinicia el contador de intentos fallidos tras una autenticacion exitosa. */
    public void resetFailedAttempts() {
        failedAttempts = 0;
    }

    /**
     * Devuelve la cuenta a su estado inicial (desbloqueada y sin intentos).
     *
     * <p>Lo invoca el endpoint de soporte de pruebas para garantizar independencia entre
     * escenarios de Cucumber.</p>
     */
    public void unlock() {
        failedAttempts = 0;
        locked = false;
    }
}
