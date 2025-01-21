package com.aluracursos.forohub.domain.topico;

import com.aluracursos.forohub.domain.curso.Curso;
import com.aluracursos.forohub.domain.respuesta.Respuesta;
import com.aluracursos.forohub.domain.usuario.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public record DatosDetalleTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        StatusTopico status,
        DatosAutor autor,
        DatosCurso curso,
        List<DatosRespuesta> respuestas
) {
    public DatosDetalleTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                new DatosAutor(topico.getAutor()),
                new DatosCurso(topico.getCurso()),
                topico.getRespuestas().stream().map(DatosRespuesta::new).toList()
        );
    }

    private record DatosAutor(
            Long id,
            String nombre,
            String correoElectronico
    ) {
        private DatosAutor(Usuario autor) {
            this(autor.getId(), autor.getNombre(), autor.getCorreoElectronico());
        }
    }

    private record DatosCurso(
            Long id,
            String nombre,
            String categoria
    ) {
        private DatosCurso(Curso curso) {
            this(curso.getId(), curso.getNombre(), curso.getCategoria());
        }
    }

    private record DatosRespuesta(
            Long id,
            String mensaje,
            LocalDateTime fechaCreacion,
            String autorNombre,
            Boolean solucion
    ) {
        private DatosRespuesta(Respuesta respuesta) {
            this(
                    respuesta.getId(),
                    respuesta.getMensaje(),
                    respuesta.getFechaCreacion(),
                    respuesta.getAutor().getNombre(),
                    respuesta.getSolucion()
            );
        }
    }
}