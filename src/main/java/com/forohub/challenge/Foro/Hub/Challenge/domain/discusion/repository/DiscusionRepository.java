package com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.repository;


import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Discusion;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscusionRepository extends JpaRepository<Discusion, Long> {

    @SuppressWarnings("null")
    Page<Discusion> findAll(Pageable pageable);

    Page<Discusion> findAllByEstadoIsNot(Estado estado, Pageable pageable);

    Boolean existsByTituloAndMensaje(String titulo, String mensaje);

    Discusion findByTitulo(String titulo);
}
