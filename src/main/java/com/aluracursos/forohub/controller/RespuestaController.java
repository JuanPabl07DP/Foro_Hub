package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.respuesta.*;
import com.aluracursos.forohub.domain.topico.TopicoRepository;
import com.aluracursos.forohub.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/respuestas")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaRespuesta> registrarRespuesta(
            @RequestBody @Valid DatosRegistroRespuesta datos,
            UriComponentsBuilder uriBuilder
    ) {
        var topico = topicoRepository.getReferenceById(datos.topicoId());
        var autor = usuarioRepository.getReferenceById(datos.autorId());

        var respuesta = new Respuesta(datos);
        respuesta.setTopico(topico);
        respuesta.setAutor(autor);

        respuestaRepository.save(respuesta);

        var uri = uriBuilder.path("/api/respuestas/{id}")
                .buildAndExpand(respuesta.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(new DatosRespuestaRespuesta(respuesta));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoRespuesta>> listadoRespuestas(
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion,
            @RequestParam(required = false) Long topicoId
    ) {
        Page<DatosListadoRespuesta> page;

        if (topicoId != null) {
            page = respuestaRepository.findByTopicoId(topicoId, paginacion)
                    .map(DatosListadoRespuesta::new);
        } else {
            page = respuestaRepository.findAll(paginacion)
                    .map(DatosListadoRespuesta::new);
        }

        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaRespuesta> actualizarRespuesta(
            @RequestBody @Valid DatosActualizarRespuesta datos
    ) {
        var respuesta = respuestaRepository.getReferenceById(datos.id());
        respuesta.actualizarDatos(datos);

        return ResponseEntity.ok(new DatosRespuestaRespuesta(respuesta));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarRespuesta(@PathVariable Long id) {
        respuestaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaRespuesta> retornaDatosRespuesta(@PathVariable Long id) {
        var respuesta = respuestaRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosRespuestaRespuesta(respuesta));
    }
}
