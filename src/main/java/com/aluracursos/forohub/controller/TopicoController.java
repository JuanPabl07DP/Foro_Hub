package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.curso.CursoRepository;
import com.aluracursos.forohub.domain.topico.*;
import com.aluracursos.forohub.domain.usuario.UsuarioRepository;
import com.aluracursos.forohub.infra.errores.ValidacionDeIntegridad;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datos,
            UriComponentsBuilder uriBuilder
    ) {
        // Validar que existan el autor y el curso
        var autor = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new ValidacionDeIntegridad("Autor no encontrado"));

        var curso = cursoRepository.findById(datos.cursoId())
                .orElseThrow(() -> new ValidacionDeIntegridad("Curso no encontrado"));

        // Validar que no exista un tópico duplicado
        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            throw new ValidacionDeIntegridad("Ya existe un tópico con el mismo título y mensaje");
        }

        // Crear y guardar el tópico
        var topico = new Topico();
        topico.setTitulo(datos.titulo());
        topico.setMensaje(datos.mensaje());
        topico.setAutor(autor);
        topico.setCurso(curso);
        topico.setStatus(StatusTopico.NO_RESPONDIDO);

        topicoRepository.save(topico);

        // Crear la URI de respuesta
        var uri = uriBuilder.path("/api/topicos/{id}")
                .buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(new DatosRespuestaTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listadoTopicos(
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion
    ) {
        var page = topicoRepository.findAll(paginacion)
                .map(DatosListadoTopico::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> detalleTopico(@PathVariable Long id) {
        var topicoOptional = topicoRepository.findById(id);

        if (!topicoOptional.isPresent()) {
            throw new ValidacionDeIntegridad("No se encontró el tópico con ID: " + id);
        }

        return ResponseEntity.ok(new DatosRespuestaTopico(topicoOptional.get()));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datos
    ) {
        var topicoOptional = topicoRepository.findById(id);

        if (!topicoOptional.isPresent()) {
            throw new ValidacionDeIntegridad("No se encontró el tópico con ID: " + id);
        }

        var topico = topicoOptional.get();

        // Validar duplicados solo si el título o mensaje cambian
        if (!datos.titulo().equals(topico.getTitulo()) || !datos.mensaje().equals(topico.getMensaje())) {
            if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
                throw new ValidacionDeIntegridad("Ya existe un tópico con el mismo título y mensaje");
            }
        }

        // Actualizar datos
        topico.setTitulo(datos.titulo());
        topico.setMensaje(datos.mensaje());
        if (datos.status() != null) {
            topico.setStatus(datos.status());
        }

        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        var topicoOptional = topicoRepository.findById(id);

        if (!topicoOptional.isPresent()) {
            throw new ValidacionDeIntegridad("No se encontró el tópico con ID: " + id);
        }

        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
