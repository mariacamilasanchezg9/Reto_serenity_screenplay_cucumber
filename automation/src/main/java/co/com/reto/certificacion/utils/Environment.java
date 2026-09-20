package co.com.reto.certificacion.utils;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;
import net.thucydides.model.environment.SystemEnvironmentVariables;

/**
 * Lector centralizado de la configuracion del ambiente.
 *
 * <p><b>Que hace:</b> obtiene las URLs del sistema bajo pruebas desde {@code serenity.conf},
 * respetando el ambiente activo que se seleccione con {@code -Denvironment=...}.</p>
 *
 * <p><b>Para que se usa:</b> ninguna clase del framework tiene URLs "quemadas". Si el servicio se
 * levanta en otro puerto o en otra maquina, basta con cambiar {@code serenity.conf} o pasar una
 * propiedad por linea de comandos.</p>
 */
public final class Environment {

    /** Clave en serenity.conf con la URL base de la aplicacion web. */
    private static final String WEB_BASE_URL_KEY = "webdriver.base.url";

    /** Clave en serenity.conf con la URL base de la API REST. */
    private static final String API_BASE_URL_KEY = "restapi.baseurl";

    /** Valor de respaldo si la configuracion no define la URL. */
    private static final String FALLBACK_URL = "http://localhost:8080";

    /** Clase de utilidad: no se instancia. */
    private Environment() {
        throw new UnsupportedOperationException("Clase de utilidad, no instanciable");
    }

    /**
     * @return la URL base de la aplicacion web (por ejemplo {@code http://localhost:8080}).
     */
    public static String webBaseUrl() {
        return propertyOrDefault(WEB_BASE_URL_KEY);
    }

    /**
     * @return la URL base de la API REST.
     */
    public static String apiBaseUrl() {
        return propertyOrDefault(API_BASE_URL_KEY);
    }

    /**
     * @return la URL completa del formulario de login web.
     */
    public static String loginPageUrl() {
        return webBaseUrl() + "/login";
    }

    /**
     * Resuelve una propiedad del ambiente activo aplicando el valor de respaldo.
     *
     * @param key clave a buscar en serenity.conf.
     * @return el valor configurado o {@link #FALLBACK_URL} si no existe.
     */
    private static String propertyOrDefault(String key) {
        EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
        return EnvironmentSpecificConfiguration.from(variables)
                .getOptionalProperty(key)
                .orElse(FALLBACK_URL);
    }
}
