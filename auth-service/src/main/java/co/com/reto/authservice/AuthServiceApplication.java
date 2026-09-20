package co.com.reto.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del microservicio de autenticacion.
 *
 * <p><b>Para que se usa:</b> levanta un servidor Tomcat embebido en el puerto 8080 que actua como
 * <i>sistema bajo pruebas</i> (SUT) del reto de automatizacion. El mismo servicio publica:</p>
 *
 * <ul>
 *   <li><b>Interfaz web</b> ({@code GET/POST /login}) para la automatizacion con Selenium.</li>
 *   <li><b>API REST</b> ({@code POST /api/auth/login}) para la automatizacion con SerenityRest.</li>
 * </ul>
 *
 * <p>Ambas caras delegan en la misma regla de negocio
 * ({@link co.com.reto.authservice.service.AuthenticationService}), por lo que los tres casos de
 * prueba (clave correcta, clave incorrecta y bloqueo por intentos fallidos) se comportan igual
 * en web y en API.</p>
 *
 * <p><b>Como se ejecuta:</b> {@code gradlew :auth-service:bootRun}</p>
 */
@SpringBootApplication
public class AuthServiceApplication {

    /**
     * Arranca el contexto de Spring Boot.
     *
     * @param args argumentos de linea de comandos (por ejemplo {@code --server.port=9090}).
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
