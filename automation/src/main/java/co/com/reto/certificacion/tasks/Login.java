package co.com.reto.certificacion.tasks;

import co.com.reto.certificacion.interactions.EnterCredentials;
import co.com.reto.certificacion.models.Credentials;
import co.com.reto.certificacion.userinterfaces.LoginPage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

/**
 * Tarea de negocio: iniciar sesion desde la interfaz web.
 *
 * <p><b>Que hace:</b> digita las credenciales y presiona el boton Ingresar.</p>
 *
 * <p><b>Para que se usa:</b> representa la intencion del usuario ("quiero iniciar sesion") sin
 * exponer como se logra. Es la Task que reutilizan los tres casos de prueba web: contrasena
 * correcta, contrasena incorrecta y bloqueo por intentos fallidos.</p>
 *
 * <p>Uso: {@code actor.attemptsTo(Login.withCredentials(credenciales));}</p>
 */
public class Login implements Task {

    private final Credentials credentials;

    /**
     * @param credentials usuario y contrasena con los que se intentara ingresar.
     */
    public Login(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Fabrica legible de la tarea.
     *
     * @param credentials usuario y contrasena con los que se intentara ingresar.
     * @return la tarea instrumentada para que aparezca como paso en el reporte de Serenity.
     */
    public static Login withCredentials(Credentials credentials) {
        return Tasks.instrumented(Login.class, credentials);
    }

    /**
     * Ejecuta el flujo de ingreso en el navegador.
     *
     * @param actor actor con la habilidad de navegar por la web.
     * @param <T>   tipo del actor.
     */
    @Override
    @Step("{0} intenta iniciar sesion con sus credenciales")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                EnterCredentials.of(credentials),
                Click.on(LoginPage.LOGIN_BUTTON)
        );
    }
}
