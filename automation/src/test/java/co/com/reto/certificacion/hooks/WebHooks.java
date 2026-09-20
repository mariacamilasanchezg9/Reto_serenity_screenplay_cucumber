package co.com.reto.certificacion.hooks;

import co.com.reto.certificacion.utils.ServiceHealthChecker;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

/**
 * Ganchos (hooks) del ciclo de vida de los escenarios WEB.
 *
 * <p><b>Que hace:</b> prepara y limpia el entorno de cada escenario etiquetado con {@code @web}.</p>
 *
 * <p><b>Para que se usa:</b> garantiza tres cosas imprescindibles para pruebas confiables:</p>
 * <ol>
 *   <li><b>Fallar rapido:</b> si el microservicio no esta arriba, el escenario falla con un
 *       mensaje claro en lugar de con un error de navegador dificil de interpretar.</li>
 *   <li><b>Independencia:</b> se reinicia el estado del servicio antes de cada escenario, de modo
 *       que el caso de bloqueo no deje la cuenta inutilizable para los demas.</li>
 *   <li><b>Liberacion de recursos:</b> se cierra el navegador al terminar.</li>
 * </ol>
 *
 * <p>Los hooks estan filtrados por la etiqueta {@code @web} para que no se ejecuten durante la
 * suite de API (que no necesita navegador).</p>
 */
public class WebHooks {

    /**
     * Verifica que el microservicio responda y lo devuelve a su estado inicial.
     *
     * <p>Se ejecuta primero ({@code order = 1}) porque no tiene sentido abrir el navegador si el
     * sistema bajo pruebas no esta disponible.</p>
     */
    @Before(value = "@web", order = 1)
    public void prepararAmbiente() {
        ServiceHealthChecker.verifyServiceIsUp();
        ServiceHealthChecker.resetServiceState();
    }

    /**
     * Monta el escenario de Screenplay con un elenco capaz de navegar por la web.
     *
     * <p>{@code OnlineCast} entrega a cada actor la habilidad {@code BrowseTheWeb} con un
     * navegador gestionado por Serenity, por lo que no hay que crear ni cerrar el driver a mano.</p>
     */
    @Before(value = "@web", order = 2)
    public void prepararElenco() {
        OnStage.setTheStage(new OnlineCast());
    }

    /**
     * Cierra los navegadores usados por los actores al finalizar el escenario.
     */
    @After(value = "@web")
    public void cerrarNavegador() {
        OnStage.drawTheCurtain();
    }
}
