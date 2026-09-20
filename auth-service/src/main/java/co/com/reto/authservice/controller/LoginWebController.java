package co.com.reto.authservice.controller;

import co.com.reto.authservice.domain.AuthStatus;
import co.com.reto.authservice.model.LoginResponse;
import co.com.reto.authservice.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador de la interfaz web de login.
 *
 * <p><b>Para que se usa:</b> renderiza el formulario HTML que automatiza Serenity con Selenium y
 * procesa su envio. Usa exactamente la misma regla de negocio que la API, de modo que los tres
 * casos de prueba se reproducen tal cual en el navegador.</p>
 *
 * <p>Rutas expuestas:</p>
 * <ul>
 *   <li>{@code GET  /login} &rarr; formulario de ingreso.</li>
 *   <li>{@code POST /login} &rarr; procesa credenciales; en exito redirige a {@code /home}.</li>
 *   <li>{@code GET  /home}  &rarr; pagina posterior al login exitoso.</li>
 *   <li>{@code GET  /}      &rarr; atajo que redirige a {@code /login}.</li>
 * </ul>
 */
@Controller
public class LoginWebController {

    private final AuthenticationService authenticationService;

    /**
     * @param authenticationService regla de negocio compartida con la API REST.
     */
    public LoginWebController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Redirige la raiz del sitio al formulario de login.
     *
     * @return instruccion de redireccion a {@code /login}.
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    /**
     * Muestra el formulario de login vacio.
     *
     * @return el nombre de la plantilla Thymeleaf {@code login.html}.
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    /**
     * Procesa el envio del formulario.
     *
     * <p>Comportamiento segun el resultado de negocio:</p>
     * <ul>
     *   <li><b>SUCCESS</b>: redirige a {@code /home} llevando el usuario y el token en atributos
     *       flash (no viajan en la URL).</li>
     *   <li><b>INVALID_CREDENTIALS</b>: vuelve a mostrar el formulario con el mensaje de error y
     *       los intentos restantes.</li>
     *   <li><b>ACCOUNT_LOCKED</b>: vuelve a mostrar el formulario con el mensaje de bloqueo.</li>
     * </ul>
     *
     * @param username           usuario escrito en el formulario.
     * @param password           contrasena escrita en el formulario.
     * @param model              modelo para renderizar de nuevo el formulario cuando hay error.
     * @param redirectAttributes atributos flash usados cuando el login es exitoso.
     * @return la vista a renderizar o la redireccion a {@code /home}.
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String password,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        LoginResponse response = authenticationService.authenticate(username, password);

        if (response.status() == AuthStatus.SUCCESS) {
            redirectAttributes.addFlashAttribute("username", response.username());
            redirectAttributes.addFlashAttribute("token", response.token());
            return "redirect:/home";
        }

        model.addAttribute("username", username);
        model.addAttribute("status", response.status().name());
        model.addAttribute("errorMessage", response.message());
        model.addAttribute("accountLocked", response.status() == AuthStatus.ACCOUNT_LOCKED);
        model.addAttribute("remainingAttempts", response.remainingAttempts());
        return "login";
    }

    /**
     * Pagina de bienvenida posterior a un login exitoso.
     *
     * <p>Si se entra directamente sin haber pasado por el formulario no hay usuario en sesion,
     * por lo que se devuelve al login. Esto evita falsos positivos en las pruebas.</p>
     *
     * @param model modelo poblado con los atributos flash del login exitoso.
     * @return la plantilla {@code home.html} o una redireccion a {@code /login}.
     */
    @GetMapping("/home")
    public String home(Model model) {
        if (!model.containsAttribute("username")) {
            return "redirect:/login";
        }
        return "home";
    }
}
