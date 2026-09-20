package co.com.reto.certificacion.interactions;

import co.com.reto.certificacion.models.Credentials;
import co.com.reto.certificacion.userinterfaces.LoginPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Enter;

/**
 * Interaccion que escribe usuario y contrasena en el formulario de login.
 *
 * <p><b>Que hace:</b> limpia ambos campos y luego digita las credenciales recibidas.</p>
 *
 * <p><b>Para que se usa:</b> el escenario de bloqueo reintenta el login varias veces sobre la
 * misma pantalla; si no se limpiaran los campos, el texto se acumularia y el caso fallaria por un
 * motivo equivocado. Encapsular esto en una Interaction evita repetir la limpieza en cada Task.</p>
 *
 * <p>Una <i>Interaction</i> es la unidad mas pequena del patron Screenplay: interactua
 * directamente con un elemento de la interfaz.</p>
 */
public class EnterCredentials implements Interaction {

    private final Credentials credentials;

    /**
     * @param credentials usuario y contrasena a digitar.
     */
    public EnterCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Fabrica legible de la interaccion.
     *
     * @param credentials usuario y contrasena a digitar.
     * @return la interaccion lista para ser ejecutada por el actor.
     */
    public static EnterCredentials of(Credentials credentials) {
        return Tasks.instrumented(EnterCredentials.class, credentials);
    }

    /**
     * Ejecuta la interaccion sobre el navegador del actor.
     *
     * @param actor actor que dispone de la habilidad de navegar por la web.
     * @param <T>   tipo del actor.
     */
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Clear.field(LoginPage.USERNAME_FIELD),
                Enter.theValue(credentials.username()).into(LoginPage.USERNAME_FIELD),
                Clear.field(LoginPage.PASSWORD_FIELD),
                Enter.theValue(credentials.password()).into(LoginPage.PASSWORD_FIELD)
        );
    }
}
