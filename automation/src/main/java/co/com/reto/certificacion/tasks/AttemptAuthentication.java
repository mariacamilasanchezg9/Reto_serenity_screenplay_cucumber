package co.com.reto.certificacion.tasks;

import co.com.reto.certificacion.models.Credentials;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

import java.util.stream.IntStream;

/**
 * Tarea de negocio: autenticarse contra la API varias veces de forma consecutiva.
 *
 * <p><b>Que hace:</b> repite el {@code POST /api/auth/login} N veces con las mismas credenciales.
 * La ultima respuesta queda disponible para las Questions de la capa API.</p>
 *
 * <p><b>Para que se usa:</b> reproduce por API el caso de prueba "bloqueo por intentos fallidos",
 * agotando los intentos permitidos por el microservicio.</p>
 *
 * <p>Uso: {@code actor.attemptsTo(AttemptAuthentication.times(3).withCredentials(credenciales));}</p>
 */
public class AttemptAuthentication implements Task {

    private final int times;
    private final Credentials credentials;

    /**
     * @param times       cantidad de peticiones consecutivas.
     * @param credentials credenciales usadas en cada peticion.
     */
    public AttemptAuthentication(int times, Credentials credentials) {
        this.times = times;
        this.credentials = credentials;
    }

    /**
     * Inicia la construccion fluida indicando cuantas peticiones se realizaran.
     *
     * @param times cantidad de peticiones consecutivas.
     * @return un constructor al que se le deben indicar las credenciales.
     */
    public static AttemptAuthenticationBuilder times(int times) {
        return new AttemptAuthenticationBuilder(times);
    }

    /**
     * Ejecuta las peticiones consecutivas.
     *
     * @param actor actor con la habilidad de consumir la API.
     * @param <T>   tipo del actor.
     */
    @Override
    @Step("{0} intenta autenticarse #times veces consecutivas en la API")
    public <T extends Actor> void performAs(T actor) {
        IntStream.rangeClosed(1, times).forEach(attempt ->
                actor.attemptsTo(Authenticate.withCredentials(credentials))
        );
    }

    /**
     * Constructor fluido de {@link AttemptAuthentication}.
     */
    public static class AttemptAuthenticationBuilder {

        private final int times;

        /**
         * @param times cantidad de peticiones consecutivas.
         */
        private AttemptAuthenticationBuilder(int times) {
            this.times = times;
        }

        /**
         * Completa la construccion con las credenciales a utilizar.
         *
         * @param credentials usuario y contrasena de cada peticion.
         * @return la tarea instrumentada lista para ejecutarse.
         */
        public AttemptAuthentication withCredentials(Credentials credentials) {
            return Tasks.instrumented(AttemptAuthentication.class, times, credentials);
        }
    }
}
