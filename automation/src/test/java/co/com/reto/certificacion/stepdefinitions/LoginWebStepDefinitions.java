package co.com.reto.certificacion.stepdefinitions;

import co.com.reto.certificacion.exceptions.LoginWebException;
import co.com.reto.certificacion.interactions.NavigateTo;
import co.com.reto.certificacion.models.Credentials;
import co.com.reto.certificacion.questions.web.ErrorMessage;
import co.com.reto.certificacion.questions.web.LockMessage;
import co.com.reto.certificacion.questions.web.LoggedUser;
import co.com.reto.certificacion.questions.web.RemainingAttempts;
import co.com.reto.certificacion.questions.web.WelcomeMessage;
import co.com.reto.certificacion.tasks.AttemptLogin;
import co.com.reto.certificacion.tasks.Login;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Traduccion de los pasos Gherkin de la funcionalidad de login WEB a acciones de Screenplay.
 *
 * <p><b>Que hace:</b> conecta cada linea del archivo {@code login_web.feature} con las Tasks y
 * Questions del framework.</p>
 *
 * <p><b>Para que se usa:</b> es la unica capa que conoce el texto de los escenarios. No contiene
 * logica de automatizacion ni selectores: solo orquesta. Gracias a eso, un cambio en la interfaz
 * no obliga a tocar este archivo.</p>
 *
 * <p>Las aserciones usan {@code orComplainWith(...)} para que, al fallar, el reporte de Serenity
 * muestre un mensaje de negocio entendible en lugar de un error tecnico.</p>
 */
public class LoginWebStepDefinitions {

    /** Nombre del actor que protagoniza los escenarios web. */
    private static final String ACTOR = "Diego";

    /**
     * Situa al actor en la pantalla de inicio de sesion.
     */
    @Dado("que el usuario se encuentra en la página de inicio de sesión")
    public void queElUsuarioSeEncuentraEnLaPaginaDeInicioDeSesion() {
        theActorCalled(ACTOR).attemptsTo(NavigateTo.theLoginPage());
    }

    /**
     * Ejecuta un unico intento de inicio de sesion.
     *
     * @param username usuario a digitar en el formulario.
     * @param password contrasena a digitar en el formulario.
     */
    @Cuando("intenta iniciar sesión con el usuario {string} y la contraseña {string}")
    public void intentaIniciarSesionCon(String username, String password) {
        theActorInTheSpotlight().attemptsTo(
                Login.withCredentials(Credentials.of(username, password))
        );
    }

    /**
     * Ejecuta varios intentos consecutivos de inicio de sesion.
     *
     * <p>Es el paso que dispara el caso de prueba de bloqueo por intentos fallidos.</p>
     *
     * @param times    cantidad de intentos consecutivos.
     * @param username usuario a digitar en cada intento.
     * @param password contrasena a digitar en cada intento.
     */
    @Cuando("intenta iniciar sesión {int} veces con el usuario {string} y la contraseña {string}")
    public void intentaIniciarSesionVariasVeces(int times, String username, String password) {
        theActorInTheSpotlight().attemptsTo(
                AttemptLogin.times(times).withCredentials(Credentials.of(username, password))
        );
    }

    /**
     * Verifica el ingreso exitoso comprobando el mensaje de bienvenida.
     *
     * @param expectedMessage mensaje de bienvenida esperado.
     */
    @Entonces("debería visualizar el mensaje de bienvenida {string}")
    public void deberiaVisualizarElMensajeDeBienvenida(String expectedMessage) {
        theActorInTheSpotlight().should(
                seeThat(WelcomeMessage.displayed(), is(equalTo(expectedMessage)))
                        .orComplainWith(LoginWebException.class,
                                "No se mostró el mensaje de bienvenida esperado: el ingreso no fue exitoso")
        );
    }

    /**
     * Verifica que el usuario autenticado sea el esperado.
     *
     * @param expectedUser nombre de usuario esperado en pantalla.
     */
    @Entonces("el usuario autenticado debería ser {string}")
    public void elUsuarioAutenticadoDeberiaSer(String expectedUser) {
        theActorInTheSpotlight().should(
                seeThat(LoggedUser.displayed(), is(equalTo(expectedUser)))
                        .orComplainWith(LoginWebException.class,
                                "El usuario mostrado en pantalla no corresponde al que inició sesión")
        );
    }

    /**
     * Verifica el mensaje de credenciales invalidas.
     *
     * @param expectedMessage mensaje de error esperado.
     */
    @Entonces("debería visualizar el mensaje de error {string}")
    public void deberiaVisualizarElMensajeDeError(String expectedMessage) {
        theActorInTheSpotlight().should(
                seeThat(ErrorMessage.displayed(), is(equalTo(expectedMessage)))
                        .orComplainWith(LoginWebException.class,
                                "La aplicación no informó correctamente el error de credenciales inválidas")
        );
    }

    /**
     * Verifica cuantos intentos le quedan al usuario antes del bloqueo.
     *
     * @param expectedAttempts cantidad de intentos restantes esperada.
     */
    @Entonces("debería ver que le quedan {int} intentos disponibles")
    public void deberiaVerQueLeQuedanIntentosDisponibles(int expectedAttempts) {
        theActorInTheSpotlight().should(
                seeThat(RemainingAttempts.displayed(), is(equalTo(expectedAttempts)))
                        .orComplainWith(LoginWebException.class,
                                "El contador de intentos restantes no coincide con lo esperado")
        );
    }

    /**
     * Verifica que la cuenta quedo bloqueada y que la pantalla lo informa.
     *
     * @param expectedMessage mensaje de bloqueo esperado.
     */
    @Entonces("debería visualizar el mensaje de bloqueo {string}")
    public void deberiaVisualizarElMensajeDeBloqueo(String expectedMessage) {
        theActorInTheSpotlight().should(
                seeThat(LockMessage.isVisible(), is(true))
                        .orComplainWith(LoginWebException.class,
                                "La cuenta no fue bloqueada luego de agotar los intentos permitidos"),
                seeThat(LockMessage.displayed(), is(equalTo(expectedMessage)))
                        .orComplainWith(LoginWebException.class,
                                "El mensaje de bloqueo mostrado no corresponde al esperado")
        );
    }
}
