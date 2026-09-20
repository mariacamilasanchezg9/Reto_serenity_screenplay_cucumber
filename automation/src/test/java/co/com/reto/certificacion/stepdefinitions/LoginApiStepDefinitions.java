package co.com.reto.certificacion.stepdefinitions;

import co.com.reto.certificacion.exceptions.LoginApiException;
import co.com.reto.certificacion.models.Credentials;
import co.com.reto.certificacion.questions.api.RemainingAttempts;
import co.com.reto.certificacion.questions.api.ResponseField;
import co.com.reto.certificacion.questions.api.ResponseStatusCode;
import co.com.reto.certificacion.questions.api.SessionToken;
import co.com.reto.certificacion.tasks.AttemptAuthentication;
import co.com.reto.certificacion.tasks.Authenticate;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Traduccion de los pasos Gherkin de la funcionalidad de login por API a acciones de Screenplay.
 *
 * <p><b>Que hace:</b> conecta cada linea del archivo {@code login_api.feature} con las Tasks y
 * Questions que consumen {@code POST /api/auth/login} mediante SerenityRest.</p>
 *
 * <p><b>Para que se usa:</b> permite validar las mismas reglas de negocio que la suite web pero
 * sin navegador: la ejecucion es mucho mas rapida y sirve como prueba de contrato del servicio.</p>
 */
public class LoginApiStepDefinitions {

    /** Nombre del actor que protagoniza los escenarios de API. */
    private static final String ACTOR = "Camila";

    /**
     * Envia una unica peticion de autenticacion.
     *
     * @param username usuario enviado en el cuerpo JSON.
     * @param password contrasena enviada en el cuerpo JSON.
     */
    @Cuando("envía la petición de autenticación con el usuario {string} y la contraseña {string}")
    public void enviaLaPeticionDeAutenticacion(String username, String password) {
        theActorCalled(ACTOR).attemptsTo(
                Authenticate.withCredentials(Credentials.of(username, password))
        );
    }

    /**
     * Envia varias peticiones consecutivas de autenticacion.
     *
     * <p>Es el paso que agota los intentos permitidos y provoca el bloqueo de la cuenta.</p>
     *
     * @param times    cantidad de peticiones consecutivas.
     * @param username usuario enviado en cada peticion.
     * @param password contrasena enviada en cada peticion.
     */
    @Cuando("envía {int} peticiones de autenticación con el usuario {string} y la contraseña {string}")
    public void enviaVariasPeticionesDeAutenticacion(int times, String username, String password) {
        theActorCalled(ACTOR).attemptsTo(
                AttemptAuthentication.times(times).withCredentials(Credentials.of(username, password))
        );
    }

    /**
     * Verifica el codigo de estado HTTP de la ultima respuesta.
     *
     * @param expectedStatusCode codigo HTTP esperado (200, 401 o 423).
     */
    @Entonces("el código de respuesta debería ser {int}")
    public void elCodigoDeRespuestaDeberiaSer(int expectedStatusCode) {
        theActorInTheSpotlight().should(
                seeThat(ResponseStatusCode.value(), is(equalTo(expectedStatusCode)))
                        .orComplainWith(LoginApiException.class,
                                "El servicio no devolvió el código HTTP esperado para este caso de prueba")
        );
    }

    /**
     * Verifica el status de negocio devuelto en el cuerpo JSON.
     *
     * @param expectedStatus SUCCESS, INVALID_CREDENTIALS o ACCOUNT_LOCKED.
     */
    @Entonces("el estado de la autenticación debería ser {string}")
    public void elEstadoDeLaAutenticacionDeberiaSer(String expectedStatus) {
        theActorInTheSpotlight().should(
                seeThat(ResponseField.called("status"), is(equalTo(expectedStatus)))
                        .orComplainWith(LoginApiException.class,
                                "El campo 'status' del cuerpo de la respuesta no corresponde al esperado")
        );
    }

    /**
     * Verifica que la autenticacion exitosa haya entregado un token de sesion.
     */
    @Entonces("debería recibir un token de sesión válido")
    public void deberiaRecibirUnTokenDeSesionValido() {
        theActorInTheSpotlight().should(
                seeThat(SessionToken.value(), is(not(nullValue())))
                        .orComplainWith(LoginApiException.class,
                                "La autenticación fue exitosa pero el servicio no entregó token de sesión")
        );
    }

    /**
     * Verifica que una autenticacion fallida no entregue token.
     */
    @Entonces("no debería recibir ningún token de sesión")
    public void noDeberiaRecibirNingunTokenDeSesion() {
        theActorInTheSpotlight().should(
                seeThat(SessionToken.value(), is(nullValue()))
                        .orComplainWith(LoginApiException.class,
                                "El servicio entregó un token de sesión pese a que la autenticación falló")
        );
    }

    /**
     * Verifica los intentos restantes reportados por la API.
     *
     * @param expectedAttempts cantidad de intentos restantes esperada.
     */
    @Entonces("los intentos restantes deberían ser {int}")
    public void losIntentosRestantesDeberianSer(int expectedAttempts) {
        theActorInTheSpotlight().should(
                seeThat(RemainingAttempts.reported(), is(equalTo(expectedAttempts)))
                        .orComplainWith(LoginApiException.class,
                                "El campo 'remainingAttempts' no refleja correctamente la política de bloqueo")
        );
    }
}
