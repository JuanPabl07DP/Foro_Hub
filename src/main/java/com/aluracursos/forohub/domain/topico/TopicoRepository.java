package com.aluracursos.forohub.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    @Query("""
            select t from Topico t
            left join fetch t.autor
            left join fetch t.curso
            left join fetch t.respuestas r
            left join fetch r.autor
            where t.id = :id
            """)
    Optional<Topico> findByIdWithDetails(@Param("id") Long id);
}