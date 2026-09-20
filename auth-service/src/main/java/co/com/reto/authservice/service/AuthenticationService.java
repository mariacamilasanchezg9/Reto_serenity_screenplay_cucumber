package co.com.reto.authservice.service;

import co.com.reto.authservice.domain.AuthStatus;
import co.com.reto.authservice.domain.UserAccount;
import co.com.reto.authservice.model.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Regla de negocio de autenticacion compartida por la web y por la API.
 *
 * <p><b>Que hace:</b> valida credenciales contra un repositorio de usuarios en memoria y aplica la
 * politica de bloqueo por intentos fallidos.</p>
 *
 * <p><b>Para que se usa:</b> es la unica fuente de verdad del SUT. Al estar centralizada aqui, los
 * tres casos de prueba del reto se comportan de forma identica sin importar el canal:</p>
 *
 * <ol>
 *   <li><b>Contrasena correcta</b> &rarr; {@link AuthStatus#SUCCESS} y token de sesion.</li>
 *   <li><b>Contrasena incorrecta</b> &rarr; {@link AuthStatus#INVALID_CREDENTIALS} indicando los
 *       intentos restantes.</li>
 *   <li><b>Bloqueo</b> &rarr; al agotar los intentos permitidos la cuenta queda en
 *       {@link AuthStatus#ACCOUNT_LOCKED} y rechaza incluso la contrasena correcta.</li>
 * </ol>
 */
@Service
public class AuthenticationService {

    /** Usuario valido precargado que utilizan los escenarios de prueba. */
    public static final String DEFAULT_USERNAME = "usuario.demo";

    /** Contrasena valida del usuario precargado. */
    public static final String DEFAULT_PASSWORD = "Clave123*";

    /** Repositorio de cuentas en memoria, indexado por nombre de usuario. */
    private final Map<String, UserAccount> accounts = new ConcurrentHashMap<>();

    /** Maximo de intentos fallidos consecutivos antes del bloqueo (configurable). */
    private final int maxAttempts;

    /**
     * Construye el servicio y precarga la cuenta de demostracion.
     *
     * @param maxAttempts valor de la propiedad {@code auth.max-failed-attempts} (3 por defecto).
     */
    public AuthenticationService(@Value("${auth.max-failed-attempts:3}") int maxAttempts) {
        this.maxAttempts = maxAttempts;
        resetAll();
    }

    /**
     * Intenta autenticar a un usuario aplicando la politica de bloqueo.
     *
     * <p>Orden de evaluacion:</p>
     * <ol>
     *   <li>Si la cuenta ya esta bloqueada se responde ACCOUNT_LOCKED sin revisar la contrasena.</li>
     *   <li>Si la contrasena es correcta se reinicia el contador y se entrega un token.</li>
     *   <li>Si es incorrecta se suma un intento; si con ese intento se alcanza el maximo, la
     *       cuenta queda bloqueada y la respuesta pasa a ser ACCOUNT_LOCKED.</li>
     * </ol>
     *
     * <p>Un usuario inexistente responde INVALID_CREDENTIALS para no revelar que cuentas existen.</p>
     *
     * @param username usuario a autenticar.
     * @param password contrasena enviada.
     * @return la respuesta con el status, el mensaje y los intentos restantes.
     */
    public synchronized LoginResponse authenticate(String username, String password) {
        UserAccount account = accounts.get(username);

        if (account == null) {
            return new LoginResponse(
                    AuthStatus.INVALID_CREDENTIALS,
                    "Usuario o contrasena incorrectos",
                    username, null, maxAttempts);
        }

        if (account.isLocked()) {
            return lockedResponse(account);
        }

        if (account.matchesPassword(password)) {
            account.resetFailedAttempts();
            return new LoginResponse(
                    AuthStatus.SUCCESS,
                    "Autenticacion exitosa",
                    account.getUsername(),
                    UUID.randomUUID().toString(),
                    maxAttempts);
        }

        account.registerFailedAttempt(maxAttempts);

        if (account.isLocked()) {
            return lockedResponse(account);
        }

        return new LoginResponse(
                AuthStatus.INVALID_CREDENTIALS,
                "Usuario o contrasena incorrectos",
                account.getUsername(),
                null,
                maxAttempts - account.getFailedAttempts());
    }

    /**
     * Construye la respuesta estandar de cuenta bloqueada.
     *
     * @param account cuenta bloqueada.
     * @return respuesta con status ACCOUNT_LOCKED y cero intentos restantes.
     */
    private LoginResponse lockedResponse(UserAccount account) {
        return new LoginResponse(
                AuthStatus.ACCOUNT_LOCKED,
                "La cuenta ha sido bloqueada por superar " + maxAttempts + " intentos fallidos",
                account.getUsername(),
                null,
                0);
    }

    /**
     * Devuelve todas las cuentas a su estado inicial.
     *
     * <p><b>Para que se usa:</b> lo invoca el endpoint de soporte de pruebas antes de cada
     * escenario de Cucumber, garantizando que un escenario de bloqueo no contamine al siguiente.</p>
     */
    public synchronized void resetAll() {
        accounts.clear();
        accounts.put(DEFAULT_USERNAME, new UserAccount(DEFAULT_USERNAME, DEFAULT_PASSWORD));
    }

    /** @return el maximo de intentos fallidos configurado. */
    public int getMaxAttempts() {
        return maxAttempts;
    }
}
