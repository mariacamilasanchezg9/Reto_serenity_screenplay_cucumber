package co.com.reto.authservice.controller;

import co.com.reto.authservice.service.AuthenticationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Endpoints de soporte exclusivos para pruebas automatizadas.
 *
 * <p><b>Para que se usa:</b> permite dejar el SUT en un estado conocido antes de cada escenario.
 * Sin esto, el escenario de bloqueo dejaria la cuenta inutilizable y los escenarios siguientes
 * fallarian, perdiendo la independencia entre pruebas.</p>
 *
 * <p><b>Importante:</b> este controlador existe solo porque el microservicio es un ambiente de
 * laboratorio. Nunca deberia publicarse en un entorno productivo.</p>
 */
@RestController
@RequestMapping(value = "/api/test-support", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestSupportController {

    private final AuthenticationService authenticationService;

    /**
     * @param authenticationService servicio cuyo estado en memoria se desea reiniciar.
     */
    public TestSupportController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Desbloquea las cuentas y pone en cero los contadores de intentos fallidos.
     *
     * @return {@code 200} confirmando el reinicio.
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> reset() {
        authenticationService.resetAll();
        return ResponseEntity.ok(Map.of(
                "status", "RESET_OK",
                "message", "Cuentas desbloqueadas y contadores de intentos reiniciados"));
    }
}
