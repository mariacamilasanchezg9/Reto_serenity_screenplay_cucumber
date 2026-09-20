package co.com.reto.certificacion.questions.api;

import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Pregunta: cuantos intentos le quedan al usuario antes del bloqueo.
 *
 * <p><b>Para que se usa:</b> permite verificar de forma progresiva el caso "bloqueo por intentos
 * fallidos": tras cada intento invalido el contador debe disminuir, y al bloquearse debe llegar
 * a cero.</p>
 */
@Subject("los intentos restantes antes del bloqueo")
public class RemainingAttempts implements Question<Integer> {

    /**
     * Fabrica legible de la pregunta.
     *
     * @return la pregunta sobre los intentos restantes.
     */
    public static RemainingAttempts reported() {
        return new RemainingAttempts();
    }

    /**
     * @param actor actor que ejecuto la peticion.
     * @return el valor del campo {@code remainingAttempts} de la ultima respuesta.
     */
    @Override
    public Integer answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().getInt("remainingAttempts");
    }
}
