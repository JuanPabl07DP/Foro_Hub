package com.aluracursos.forohub.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DatosErrorValidacion> tratarError400(MethodArgumentNotValidException e) {
        var errores = e.getFieldErrors().stream().map(DatosErrorCampo::new).toList();
        return ResponseEntity.badRequest().body(new DatosErrorValidacion(errores));
    }

    @ExceptionHandler(ValidacionDeIntegridad.class)
    public ResponseEntity<DatosErrorValidacion> tratarErrorDeIntegridad(ValidacionDeIntegridad e) {
        return ResponseEntity.badRequest()
                .body(new DatosErrorValidacion(e.getMessage()));
    }

    private record DatosErrorValidacion(String mensaje, List<DatosErrorCampo> errores) {
        public DatosErrorValidacion(List<DatosErrorCampo> errores) {
            this("Error de validación", errores);
        }

        public DatosErrorValidacion(String mensaje) {
            this(mensaje, new ArrayList<>());
        }
    }

    private record DatosErrorCampo(String campo, String error) {
        public DatosErrorCampo(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
