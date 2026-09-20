package co.com.reto.certificacion.runners;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Runner de la suite de automatizacion de API.
 *
 * <p><b>Que hace:</b> ejecuta los escenarios Gherkin de
 * {@code src/test/resources/features/api} contra el endpoint REST del microservicio.</p>
 *
 * <p><b>Para que se usa:</b> permite lanzar solo las pruebas de servicios, que no requieren
 * navegador y por lo tanto son las ideales para una tuberia de integracion continua. Se ejecuta
 * desde IntelliJ o con {@code gradlew apiTests}.</p>
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/api")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME,
        value = "co.com.reto.certificacion.stepdefinitions,co.com.reto.certificacion.hooks")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME,
        value = "io.cucumber.core.plugin.SerenityReporterParallel,pretty,timeline:target/timeline-api")
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "@api")
public class ApiLoginRunner {
}
