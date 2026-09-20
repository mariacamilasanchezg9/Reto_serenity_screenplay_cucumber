package co.com.reto.certificacion.questions.web;

import co.com.reto.certificacion.userinterfaces.HomePage;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

/**
 * Pregunta: que usuario aparece como autenticado en la pantalla de inicio.
 *
 * <p><b>Para que se usa:</b> refuerza el caso "contrasena correcta" comprobando no solo que se
 * ingreso, sino que se ingreso con el usuario esperado.</p>
 */
@Subject("el usuario autenticado")
public class LoggedUser implements Question<String> {

    /**
     * Fabrica legible de la pregunta.
     *
     * @return la pregunta sobre el usuario autenticado.
     */
    public static LoggedUser displayed() {
        return new LoggedUser();
    }

    /**
     * @param actor actor que observa la pantalla.
     * @return el nombre de usuario mostrado en la pagina de inicio.
     */
    @Override
    public String answeredBy(Actor actor) {
        return Text.of(HomePage.LOGGED_USER).answeredBy(actor);
    }
}
