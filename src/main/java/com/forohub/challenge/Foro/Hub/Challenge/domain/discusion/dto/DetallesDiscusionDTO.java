package com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.dto;

import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Discusion;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Estado;

import java.time.LocalDateTime;

public record DetallesDiscusionDTO(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        LocalDateTime ultimaActualizacion,
        Estado estado,
        String usuario

) {

    public DetallesDiscusionDTO(Discusion discusion) {
        this(discusion.getId(),
                discusion.getTitulo(),
                discusion.getMensaje(),
                discusion.getFechaCreacion(),
                discusion.getUltimaActualizacion(),
                discusion.getEstado(),
                discusion.getUsuario().getUsername()
        );
    }

}