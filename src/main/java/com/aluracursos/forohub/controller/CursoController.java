package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.curso.*;
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
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaCurso> registrarCurso(
            @RequestBody @Valid DatosRegistroCurso datos,
            UriComponentsBuilder uriBuilder
    ) {
        if (cursoRepository.existsByNombre(datos.nombre())) {
            throw new RuntimeException("Ya existe un curso con ese nombre");
        }

        var curso = new Curso(datos);
        cursoRepository.save(curso);

        var uri = uriBuilder.path("/api/cursos/{id}")
                .buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(new DatosRespuestaCurso(curso));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoCurso>> listadoCursos(
            @PageableDefault(size = 10, sort = "nombre") Pageable paginacion,
            @RequestParam(required = false) String nombre
    ) {
        Page<DatosListadoCurso> page;

        if (nombre != null) {
            page = cursoRepository.findByNombreContainingIgnoreCase(nombre, paginacion)
                    .map(DatosListadoCurso::new);
        } else {
            page = cursoRepository.findAll(paginacion)
                    .map(DatosListadoCurso::new);
        }

        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaCurso> actualizarCurso(
            @RequestBody @Valid DatosActualizarCurso datos
    ) {
        var curso = cursoRepository.getReferenceById(datos.id());
        curso.actualizarDatos(datos);

        return ResponseEntity.ok(new DatosRespuestaCurso(curso));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaCurso> retornaDatosCurso(@PathVariable Long id) {
        var curso = cursoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosRespuestaCurso(curso));
    }
}