package co.com.reto.certificacion.interactions;

import co.com.reto.certificacion.models.Credentials;
import co.com.reto.certificacion.utils.ApiEndpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;

/**
 * Interaccion que envia la peticion de autenticacion a la API REST.
 *
 * <p><b>Que hace:</b> ejecuta un {@code POST /api/auth/login} con las credenciales serializadas
 * como JSON, usando {@code serenity-screenplay-rest} (SerenityRest).</p>
 *
 * <p><b>Para que se usa:</b> aisla el detalle del protocolo HTTP (verbo, cabeceras, cuerpo) en un
 * solo lugar. Las Tasks solo expresan la intencion de negocio: "autenticarse".</p>
 *
 * <p>Serenity registra automaticamente la peticion y la respuesta en el reporte, lo que facilita
 * el diagnostico cuando un escenario falla.</p>
 */
public class SendLoginRequest implements Interaction {

    private final Credentials credentials;

    /**
     * @param credentials credenciales a enviar en el cuerpo de la peticion.
     */
    public SendLoginRequest(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Fabrica legible de la interaccion.
     *
     * @param credentials credenciales a enviar.
     * @return la interaccion lista para ser ejecutada por el actor.
     */
    public static SendLoginRequest with(Credentials credentials) {
        return Tasks.instrumented(SendLoginRequest.class, credentials);
    }

    /**
     * Ejecuta la peticion HTTP con la habilidad {@code CallAnApi} del actor.
     *
     * @param actor actor con la habilidad de consumir la API.
     * @param <T>   tipo del actor.
     */
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to(ApiEndpoints.LOGIN)
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .accept(ContentType.JSON)
                                .body(credentials.asJsonBody()))
        );
    }
}
