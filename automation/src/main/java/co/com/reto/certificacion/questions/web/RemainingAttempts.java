package co.com.reto.certificacion.questions.web;

import co.com.reto.certificacion.userinterfaces.LoginPage;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

/**
 * Pregunta: cuantos intentos le quedan al usuario segun el mensaje de la pantalla.
 *
 * <p><b>Que hace:</b> lee el texto "Intentos restantes: N" y devuelve unicamente el numero.</p>
 *
 * <p><b>Para que se usa:</b> permite afirmar sobre un entero en lugar de comparar cadenas, lo que
 * hace el escenario mas legible y menos fragil ante cambios de redaccion del mensaje.</p>
 */
@Subject("los intentos restantes mostrados en pantalla")
public class RemainingAttempts implements Question<Integer> {

    /** Todo lo que no sea un digito se descarta del texto leido. */
    private static final String NON_DIGITS = "[^0-9]";

    /**
     * Fabrica legible de la pregunta.
     *
     * @return la pregunta sobre los intentos restantes.
     */
    public static RemainingAttempts displayed() {
        return new RemainingAttempts();
    }

    /**
     * @param actor actor que observa la pantalla.
     * @return el numero de intentos restantes; {@code -1} si el mensaje no esta presente.
     */
    @Override
    public Integer answeredBy(Actor actor) {
        String text = Text.of(LoginPage.REMAINING_ATTEMPTS).answeredBy(actor);
        String digits = (text == null) ? "" : text.replaceAll(NON_DIGITS, "");
        return digits.isEmpty() ? -1 : Integer.parseInt(digits);
    }
}
