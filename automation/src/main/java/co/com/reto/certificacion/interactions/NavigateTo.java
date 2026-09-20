package co.com.reto.certificacion.interactions;

import co.com.reto.certificacion.userinterfaces.LoginPage;
import co.com.reto.certificacion.utils.Environment;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Interaccion de navegacion hacia la pantalla de login.
 *
 * <p><b>Que hace:</b> abre la URL del formulario y espera a que el titulo sea visible antes de
 * devolver el control.</p>
 *
 * <p><b>Para que se usa:</b> la URL se resuelve desde {@code serenity.conf} a traves de
 * {@link Environment}, por lo que las pruebas pueden apuntar a otro puerto o ambiente sin tocar
 * codigo. La espera explicita evita pruebas intermitentes por digitar antes de que cargue la
 * pagina.</p>
 */
public class NavigateTo implements Interaction {

    /** Segundos maximos de espera a que la pantalla de login este visible. */
    private static final int TIMEOUT_SECONDS = 15;

    /**
     * Fabrica legible de la interaccion.
     *
     * @return la interaccion que abre la pantalla de login.
     */
    public static NavigateTo theLoginPage() {
        return Tasks.instrumented(NavigateTo.class);
    }

    /**
     * Abre la pagina de login y espera a que este disponible.
     *
     * @param actor actor con la habilidad de navegar por la web.
     * @param <T>   tipo del actor.
     */
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.url(Environment.loginPageUrl()),
                WaitUntil.the(LoginPage.PAGE_TITLE, isVisible()).forNoMoreThan(TIMEOUT_SECONDS).seconds()
        );
    }
}
