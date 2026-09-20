package co.com.reto.certificacion.questions.web;

import co.com.reto.certificacion.userinterfaces.LoginPage;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

/**
 * Pregunta: que mensaje de bloqueo muestra el formulario de login.
 *
 * <p><b>Para que se usa:</b> valida el caso de prueba "bloqueo por intentos fallidos". Es el
 * indicador visual de que la cuenta quedo inhabilitada tras agotar los intentos permitidos.</p>
 */
@Subject("el mensaje de cuenta bloqueada")
public class LockMessage implements Question<String> {

    /**
     * Fabrica legible de la pregunta por el texto del mensaje.
     *
     * @return la pregunta sobre el texto del mensaje de bloqueo.
     */
    public static LockMessage displayed() {
        return new LockMessage();
    }

    /**
     * Pregunta complementaria sobre la visibilidad del mensaje de bloqueo.
     *
     * @return {@code true} si el mensaje de bloqueo esta visible en pantalla.
     */
    public static Question<Boolean> isVisible() {
        return actor -> LoginPage.LOCK_MESSAGE.resolveFor(actor).isVisible();
    }

    /**
     * @param actor actor que observa la pantalla.
     * @return el texto del mensaje de bloqueo, o cadena vacia si no se esta mostrando.
     */
    @Override
    public String answeredBy(Actor actor) {
        return Text.of(LoginPage.LOCK_MESSAGE).answeredBy(actor);
    }
}
