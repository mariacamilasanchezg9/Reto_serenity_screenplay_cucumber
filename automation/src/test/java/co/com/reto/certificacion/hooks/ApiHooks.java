package co.com.reto.certificacion.hooks;

import co.com.reto.certificacion.utils.Environment;
import co.com.reto.certificacion.utils.ServiceHealthChecker;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

/**
 * Ganchos (hooks) del ciclo de vida de los escenarios de API.
 *
 * <p><b>Que hace:</b> prepara el elenco de actores con la habilidad de consumir la API REST y
 * reinicia el estado del microservicio antes de cada escenario etiquetado con {@code @api}.</p>
 *
 * <p><b>Para que se usa:</b> al usar {@code Cast.whereEveryoneCan(CallAnApi.at(...))} en lugar de
 * un elenco web, la suite de API se ejecuta sin abrir ningun navegador: es mas rapida y puede
 * correr en servidores sin interfaz grafica.</p>
 */
public class ApiHooks {

    /**
     * Verifica la disponibilidad del microservicio y reinicia su estado en memoria.
     */
    @Before(value = "@api", order = 1)
    public void prepararAmbiente() {
        ServiceHealthChecker.verifyServiceIsUp();
        ServiceHealthChecker.resetServiceState();
    }

    /**
     * Monta el escenario de Screenplay con actores capaces de llamar a la API.
     *
     * <p>La URL base se resuelve desde {@code serenity.conf}, por lo que la misma suite puede
     * apuntar a local, a un ambiente de pruebas o a un contenedor sin cambiar codigo.</p>
     */
    @Before(value = "@api", order = 2)
    public void prepararElenco() {
        OnStage.setTheStage(Cast.whereEveryoneCan(CallAnApi.at(Environment.apiBaseUrl())));
    }

    /**
     * Libera los actores al finalizar el escenario.
     */
    @After(value = "@api")
    public void finalizarEscenario() {
        OnStage.drawTheCurtain();
    }
}
