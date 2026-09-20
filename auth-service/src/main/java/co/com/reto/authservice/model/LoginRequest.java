package co.com.reto.authservice.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload JSON de entrada del endpoint {@code POST /api/auth/login}.
 *
 * <p><b>Para que se usa:</b> representa las credenciales que envia el cliente. Las anotaciones de
 * validacion hacen que una peticion sin usuario o sin contrasena sea rechazada con
 * {@code HTTP 400}, sin llegar a la logica de negocio.</p>
 *
 * @param username nombre de usuario; obligatorio.
 * @param password contrasena del usuario; obligatoria.
 */
public record LoginRequest(

        @NotBlank(message = "El usuario es obligatorio")
        String username,

        @NotBlank(message = "La contrasena es obligatoria")
        String password) {
}
