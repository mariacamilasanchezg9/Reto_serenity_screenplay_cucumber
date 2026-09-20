package co.com.reto.certificacion.questions.api;

import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Pregunta: que token de sesion devolvio la API.
 *
 * <p><b>Para que se usa:</b> complementa el caso "contrasena correcta". No basta con recibir un
 * {@code 200}: la autenticacion solo es util si entrega un token. En los casos fallidos este
 * campo debe venir vacio, lo que tambien se valida.</p>
 */
@Subject("el token de sesion devuelto por la API")
public class SessionToken implements Question<String> {

    /**
     * Fabrica legible de la pregunta.
     *
     * @return la pregunta sobre el token de sesion.
     */
    public static SessionToken value() {
        return new SessionToken();
    }

    /**
     * @param actor actor que ejecuto la peticion.
     * @return el token entregado, o {@code null} cuando la autenticacion no fue exitosa.
     */
    @Override
    public String answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().getString("token");
    }
}
