package co.com.reto.certificacion.questions.api;

import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Pregunta: cual fue el codigo HTTP de la ultima respuesta de la API.
 *
 * <p><b>Para que se usa:</b> es la asercion principal de los tres casos de prueba de API:</p>
 * <ul>
 *   <li>{@code 200} contrasena correcta.</li>
 *   <li>{@code 401} contrasena incorrecta.</li>
 *   <li>{@code 423} cuenta bloqueada por intentos fallidos.</li>
 * </ul>
 *
 * <p>{@code SerenityRest.lastResponse()} guarda la respuesta de la ultima peticion ejecutada por
 * el actor, por lo que no hace falta pasarla manualmente entre pasos.</p>
 */
@Subject("el codigo de estado HTTP de la respuesta")
public class ResponseStatusCode implements Question<Integer> {

    /**
     * Fabrica legible de la pregunta.
     *
     * @return la pregunta sobre el codigo HTTP.
     */
    public static ResponseStatusCode value() {
        return new ResponseStatusCode();
    }

    /**
     * @param actor actor que ejecuto la peticion.
     * @return el codigo de estado HTTP de la ultima respuesta.
     */
    @Override
    public Integer answeredBy(Actor actor) {
        return SerenityRest.lastResponse().statusCode();
    }
}
