package co.com.reto.certificacion.questions.web;

import co.com.reto.certificacion.userinterfaces.HomePage;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

/**
 * Pregunta: que mensaje de bienvenida se esta mostrando.
 *
 * <p><b>Para que se usa:</b> valida el caso de prueba "contrasena correcta" en la interfaz web.
 * Si el login fallo, el actor seguiria en {@code /login} y la respuesta seria vacia.</p>
 *
 * <p>Uso: {@code actor.asksFor(WelcomeMessage.displayed())}</p>
 */
@Subject("el mensaje de bienvenida")
public class WelcomeMessage implements Question<String> {

    /**
     * Fabrica legible de la pregunta.
     *
     * @return la pregunta sobre el mensaje de bienvenida.
     */
    public static WelcomeMessage displayed() {
        return new WelcomeMessage();
    }

    /**
     * @param actor actor que observa la pantalla.
     * @return el texto del mensaje de bienvenida, o cadena vacia si no esta presente.
     */
    @Override
    public String answeredBy(Actor actor) {
        return Text.of(HomePage.WELCOME_MESSAGE).answeredBy(actor);
    }
}
