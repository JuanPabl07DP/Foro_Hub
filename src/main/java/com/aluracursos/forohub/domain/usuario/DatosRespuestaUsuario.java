package com.aluracursos.forohub.domain.usuario;

public record DatosRespuestaUsuario(
        Long id,
        String nombre,
        String correoElectronico
) {
    public DatosRespuestaUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNombre(), usuario.getCorreoElectronico());
    }
}
