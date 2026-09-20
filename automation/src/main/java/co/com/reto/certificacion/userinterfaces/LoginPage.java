package co.com.reto.certificacion.userinterfaces;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

/**
 * Mapa de elementos del formulario de login ({@code /login}).
 *
 * <p><b>Que hace:</b> concentra los localizadores de la pantalla de ingreso.</p>
 *
 * <p><b>Para que se usa:</b> es la capa <i>userinterfaces</i> del patron Screenplay. Ninguna Task
 * ni Question conoce un selector: todas usan estos {@link Target}. Si el front cambia un
 * identificador, solo se modifica este archivo.</p>
 *
 * <p>Extiende {@link PageObject} para poder declarar la URL por defecto y abrirla con la
 * interaccion {@code Open}.</p>
 */
public class LoginPage extends PageObject {

    /** Campo de texto donde se escribe el nombre de usuario. */
    public static final Target USERNAME_FIELD =
            Target.the("campo de usuario").locatedBy("#username");

    /** Campo de texto donde se escribe la contrasena. */
    public static final Target PASSWORD_FIELD =
            Target.the("campo de contrasena").locatedBy("#password");

    /** Boton que envia el formulario de autenticacion. */
    public static final Target LOGIN_BUTTON =
            Target.the("boton Ingresar").locatedBy("#loginButton");

    /** Mensaje mostrado cuando las credenciales son incorrectas. */
    public static final Target ERROR_MESSAGE =
            Target.the("mensaje de credenciales invalidas").locatedBy("#errorMessage");

    /** Mensaje mostrado cuando la cuenta queda bloqueada por intentos fallidos. */
    public static final Target LOCK_MESSAGE =
            Target.the("mensaje de cuenta bloqueada").locatedBy("#lockMessage");

    /** Texto informativo con los intentos que restan antes del bloqueo. */
    public static final Target REMAINING_ATTEMPTS =
            Target.the("intentos restantes").locatedBy("#remainingAttempts");

    /** Titulo de la pantalla, usado para confirmar que la pagina cargo. */
    public static final Target PAGE_TITLE =
            Target.the("titulo de la pagina de login").locatedBy("#pageTitle");
}
