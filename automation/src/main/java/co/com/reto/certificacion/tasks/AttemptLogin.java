package co.com.reto.certificacion.tasks;

import co.com.reto.certificacion.interactions.NavigateTo;
import co.com.reto.certificacion.models.Credentials;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

import java.util.stream.IntStream;

/**
 * Tarea de negocio: intentar iniciar sesion varias veces de forma consecutiva.
 *
 * <p><b>Que hace:</b> repite el flujo de login N veces con las mismas credenciales, volviendo al
 * formulario entre intento e intento.</p>
 *
 * <p><b>Para que se usa:</b> es el motor del caso de prueba "bloqueo por intentos fallidos".
 * Expresa en una sola linea del step definition lo que de otro modo serian varios pasos repetidos
 * en el archivo .feature.</p>
 *
 * <p>Uso: {@code actor.attemptsTo(AttemptLogin.times(3).withCredentials(credenciales));}</p>
 */
public class AttemptLogin implements Task {

    private final int times;
    private final Credentials credentials;

    /**
     * @param times       cantidad de intentos consecutivos.
     * @param credentials credenciales usadas en cada intento.
     */
    public AttemptLogin(int times, Credentials credentials) {
        this.times = times;
        this.credentials = credentials;
    }

    /**
     * Inicia la construccion fluida de la tarea indicando cuantos intentos se realizaran.
     *
     * @param times cantidad de intentos consecutivos.
     * @return un constructor al que se le deben indicar las credenciales.
     */
    public static AttemptLoginBuilder times(int times) {
        return new AttemptLoginBuilder(times);
    }

    /**
     * Repite el intento de login la cantidad de veces indicada.
     *
     * @param actor actor con la habilidad de navegar por la web.
     * @param <T>   tipo del actor.
     */
    @Override
    @Step("{0} intenta iniciar sesion #times veces consecutivas")
    public <T extends Actor> void performAs(T actor) {
        IntStream.rangeClosed(1, times).forEach(attempt ->
                actor.attemptsTo(
                        NavigateTo.theLoginPage(),
                        Login.withCredentials(credentials)
                )
        );
    }

    /**
     * Constructor fluido de {@link AttemptLogin}.
     *
     * <p>Permite escribir {@code AttemptLogin.times(3).withCredentials(...)}, una sintaxis que se
     * lee como una frase de negocio.</p>
     */
    public static class AttemptLoginBuilder {

        private final int times;

        /**
         * @param times cantidad de intentos consecutivos.
         */
        private AttemptLoginBuilder(int times) {
            this.times = times;
        }

        /**
         * Completa la construccion de la tarea con las credenciales a utilizar.
         *
         * @param credentials usuario y contrasena de cada intento.
         * @return la tarea instrumentada lista para ejecutarse.
         */
        public AttemptLogin withCredentials(Credentials credentials) {
            return Tasks.instrumented(AttemptLogin.class, times, credentials);
        }
    }
}
