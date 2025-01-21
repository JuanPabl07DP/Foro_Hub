package com.aluracursos.forohub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DatosRegistroUsuario(
        @NotBlank
        String nombre,

        @NotBlank
        @Email
        String correoElectronico,

        @NotBlank
        @Size(min = 8)
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "La contraseña debe contener al menos un número, una letra mayúscula, " +
                        "una letra minúscula, un carácter especial y no debe contener espacios")
        String contrasena
) {}
