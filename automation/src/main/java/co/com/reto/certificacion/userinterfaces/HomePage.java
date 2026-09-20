package co.com.reto.certificacion.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Mapa de elementos de la pantalla posterior a un login exitoso ({@code /home}).
 *
 * <p><b>Para que se usa:</b> permite a las Questions confirmar el caso de prueba
 * "contrasena correcta" leyendo el mensaje de bienvenida y el usuario autenticado.</p>
 */
public class HomePage {

    /** Titulo de bienvenida que confirma la autenticacion exitosa. */
    public static final Target WELCOME_MESSAGE =
            Target.the("mensaje de bienvenida").locatedBy("#welcomeMessage");

    /** Nombre del usuario autenticado mostrado en pantalla. */
    public static final Target LOGGED_USER =
            Target.the("usuario autenticado").locatedBy("#loggedUser");

    /** Token de sesion entregado tras el login exitoso. */
    public static final Target SESSION_TOKEN =
            Target.the("token de sesion").locatedBy("#sessionToken");

    /** Clase de solo localizadores: no se instancia. */
    private HomePage() {
        throw new UnsupportedOperationException("Clase de localizadores, no instanciable");
    }
}
