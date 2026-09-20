package co.com.reto.certificacion.questions.web;

import co.com.reto.certificacion.userinterfaces.LoginPage;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

/**
 * Pregunta: que mensaje de error muestra el formulario de login.
 *
 * <p><b>Para que se usa:</b> valida el caso de prueba "contrasena incorrecta" comprobando que la
 * aplicacion informa al usuario sin permitirle el ingreso.</p>
 */
@Subject("el mensaje de error del login")
public class ErrorMessage implements Question<String> {

    /**
     * Fabrica legible de la pregunta.
     *
     * @return la pregunta sobre el mensaje de error.
     */
    public static ErrorMessage displayed() {
        return new ErrorMessage();
    }

    /**
     * @param actor actor que observa la pantalla.
     * @return el texto del mensaje de error, o cadena vacia si no se esta mostrando.
     */
    @Override
    public String answeredBy(Actor actor) {
        return Text.of(LoginPage.ERROR_MESSAGE).answeredBy(actor);
    }
}
