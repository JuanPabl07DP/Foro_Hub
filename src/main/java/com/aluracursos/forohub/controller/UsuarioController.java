package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaUsuario> registrarUsuario(
            @RequestBody @Valid DatosRegistroUsuario datos,
            UriComponentsBuilder uriBuilder
    ) {
        if (usuarioRepository.existsByCorreoElectronico(datos.correoElectronico())) {
            throw new RuntimeException("Ya existe un usuario con ese correo electrónico");
        }

        var usuario = new Usuario(datos);
        usuario.setContrasena(passwordEncoder.encode(datos.contrasena()));
        usuarioRepository.save(usuario);

        var uri = uriBuilder.path("/api/usuarios/{id}")
                .buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(new DatosRespuestaUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaUsuario>> listadoUsuarios(
            @PageableDefault(size = 10, sort = "nombre") Pageable paginacion
    ) {
        var page = usuarioRepository.findAll(paginacion)
                .map(DatosRespuestaUsuario::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaUsuario> actualizarUsuario(
            @RequestBody @Valid DatosActualizarUsuario datos
    ) {
        var usuario = usuarioRepository.getReferenceById(datos.id());

        if (datos.nombre() != null) {
            usuario.setNombre(datos.nombre());
        }
        if (datos.correoElectronico() != null &&
                !datos.correoElectronico().equals(usuario.getCorreoElectronico())) {
            if (usuarioRepository.existsByCorreoElectronico(datos.correoElectronico())) {
                throw new RuntimeException("Ya existe un usuario con ese correo electrónico");
            }
            usuario.setCorreoElectronico(datos.correoElectronico());
        }

        return ResponseEntity.ok(new DatosRespuestaUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaUsuario> retornaDatosUsuario(@PathVariable Long id) {
        var usuario = usuarioRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosRespuestaUsuario(usuario));
    }
}
