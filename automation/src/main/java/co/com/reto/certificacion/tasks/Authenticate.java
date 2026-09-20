package co.com.reto.certificacion.tasks;

import co.com.reto.certificacion.interactions.SendLoginRequest;
import co.com.reto.certificacion.models.Credentials;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

/**
 * Tarea de negocio: autenticarse contra la API REST.
 *
 * <p><b>Que hace:</b> delega en la interaccion {@link SendLoginRequest} el envio del
 * {@code POST /api/auth/login}.</p>
 *
 * <p><b>Para que se usa:</b> es la Task que comparten los tres casos de prueba de API. El step
 * definition solo expresa la intencion; el detalle HTTP queda oculto en la capa de interacciones.</p>
 *
 * <p>Uso: {@code actor.attemptsTo(Authenticate.withCredentials(credenciales));}</p>
 */
public class Authenticate implements Task {

    private final Credentials credentials;

    /**
     * @param credentials credenciales enviadas a la API.
     */
    public Authenticate(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Fabrica legible de la tarea.
     *
     * @param credentials credenciales enviadas a la API.
     * @return la tarea instrumentada para el reporte de Serenity.
     */
    public static Authenticate withCredentials(Credentials credentials) {
        return Tasks.instrumented(Authenticate.class, credentials);
    }

    /**
     * Envia la peticion de autenticacion.
     *
     * @param actor actor con la habilidad de consumir la API.
     * @param <T>   tipo del actor.
     */
    @Override
    @Step("{0} se autentica en la API con sus credenciales")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(SendLoginRequest.with(credentials));
    }
}
