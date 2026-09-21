package co.com.reto.certificacion.runners;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Runner de la suite de automatizacion WEB.
 *
 * <p><b>Que hace:</b> le indica a JUnit 5 que ejecute los escenarios Gherkin ubicados en
 * {@code src/test/resources/features/web} usando el motor de Cucumber.</p>
 *
 * <p><b>Para que se usa:</b> permite lanzar solo las pruebas de interfaz grafica, ya sea desde
 * IntelliJ (clic derecho &rarr; Run) o desde consola con {@code gradlew webTests}.</p>
 *
 * <p>Configuracion aplicada:</p>
 * <ul>
 *   <li><b>glue</b>: paquete donde Cucumber busca step definitions y hooks.</li>
 *   <li><b>plugin</b>: {@code SerenityReporterParallel} alimenta el reporte HTML de Serenity.</li>
 *   <li><b>filter.tags</b>: restringe la ejecucion a los escenarios etiquetados {@code @web}.</li>
 * </ul>
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/web")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME,
        value = "co.com.reto.certificacion.stepdefinitions,co.com.reto.certificacion.hooks")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME,
        value = "io.cucumber.core.plugin.SerenityReporterParallel,pretty,timeline:target/timeline-web")
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "lkl")
public class WebLoginRunner {
}
