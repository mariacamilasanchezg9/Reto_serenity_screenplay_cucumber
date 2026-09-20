package co.com.reto.certificacion.questions.api;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Pregunta generica: que valor tiene un campo del cuerpo JSON de la ultima respuesta.
 *
 * <p><b>Que hace:</b> extrae un campo usando notacion JsonPath (por ejemplo {@code status},
 * {@code message} o {@code remainingAttempts}).</p>
 *
 * <p><b>Para que se usa:</b> evita crear una Question por cada campo del contrato. Con una sola
 * clase se validan {@code status}, {@code message}, {@code username} y cualquier campo futuro.</p>
 *
 * <p>Uso: {@code actor.asksFor(ResponseField.called("status"))}</p>
 */
public class ResponseField implements Question<String> {

    private final String jsonPath;

    /**
     * @param jsonPath ruta JsonPath del campo dentro del cuerpo de la respuesta.
     */
    public ResponseField(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    /**
     * Fabrica legible de la pregunta.
     *
     * @param jsonPath nombre o ruta del campo a consultar.
     * @return la pregunta sobre ese campo.
     */
    public static ResponseField called(String jsonPath) {
        return new ResponseField(jsonPath);
    }

    /**
     * @param actor actor que ejecuto la peticion.
     * @return el valor del campo como texto, o {@code null} si el campo no viene informado.
     */
    @Override
    public String answeredBy(Actor actor) {
        Object value = SerenityRest.lastResponse().jsonPath().get(jsonPath);
        return value == null ? null : String.valueOf(value);
    }

    /**
     * @return una descripcion legible que Serenity muestra en el reporte.
     */
    @Override
    public String getSubject() {
        return "el campo '" + jsonPath + "' de la respuesta";
    }
}
