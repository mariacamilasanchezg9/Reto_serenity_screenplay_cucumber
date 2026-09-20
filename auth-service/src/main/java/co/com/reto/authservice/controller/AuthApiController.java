package co.com.reto.authservice.controller;

import co.com.reto.authservice.domain.AuthStatus;
import co.com.reto.authservice.model.LoginRequest;
import co.com.reto.authservice.model.LoginResponse;
import co.com.reto.authservice.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * API REST de autenticacion.
 *
 * <p><b>Para que se usa:</b> es el endpoint que consume la automatizacion de API con SerenityRest.
 * Traduce el resultado de negocio a codigos HTTP que las pruebas pueden afirmar directamente:</p>
 *
 * <table border="1">
 *   <caption>Mapeo de negocio a HTTP</caption>
 *   <tr><th>Caso</th><th>Status de negocio</th><th>Codigo HTTP</th></tr>
 *   <tr><td>Contrasena correcta</td><td>SUCCESS</td><td>200 OK</td></tr>
 *   <tr><td>Contrasena incorrecta</td><td>INVALID_CREDENTIALS</td><td>401 Unauthorized</td></tr>
 *   <tr><td>Cuenta bloqueada</td><td>ACCOUNT_LOCKED</td><td>423 Locked</td></tr>
 * </table>
 */
@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthApiController {

    private final AuthenticationService authenticationService;

    /**
     * @param authenticationService regla de negocio compartida con la interfaz web.
     */
    public AuthApiController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Autentica un usuario a partir de un JSON con usuario y contrasena.
     *
     * @param request credenciales enviadas por el cliente; validadas antes de entrar al metodo.
     * @return {@code 200} si las credenciales son correctas, {@code 401} si son invalidas y
     *         {@code 423} si la cuenta esta bloqueada por intentos fallidos.
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request.username(), request.password());
        return ResponseEntity.status(httpStatusFor(response.status())).body(response);
    }

    /**
     * Endpoint de salud usado por los hooks de la automatizacion para verificar, antes de correr
     * los escenarios, que el microservicio esta arriba y dar un mensaje claro si no lo esta.
     *
     * @return {@code 200} con el estado del servicio y los intentos maximos configurados.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "maxFailedAttempts", authenticationService.getMaxAttempts()));
    }

    /**
     * Traduce el resultado de negocio al codigo HTTP correspondiente.
     *
     * @param status resultado devuelto por el servicio de autenticacion.
     * @return codigo HTTP equivalente.
     */
    private HttpStatus httpStatusFor(AuthStatus status) {
        return switch (status) {
            case SUCCESS -> HttpStatus.OK;
            case INVALID_CREDENTIALS -> HttpStatus.UNAUTHORIZED;
            case ACCOUNT_LOCKED -> HttpStatus.LOCKED;
        };
    }
}
