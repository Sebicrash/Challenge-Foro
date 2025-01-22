package com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.repository;


import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.Respuesta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    Page<Respuesta> findAllByDiscusionId(Long discusionId, Pageable pageable);

    Page<Respuesta> findAllByUsuarioId(Long usuarioId, Pageable pageable);

    Respuesta getReferenceByDiscusionId(Long id);

    @SuppressWarnings("null")
    Respuesta getReferenceById(Long id);
}