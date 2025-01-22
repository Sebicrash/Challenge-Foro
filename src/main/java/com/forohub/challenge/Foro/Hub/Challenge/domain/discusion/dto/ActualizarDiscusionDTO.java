package com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.dto;


import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Estado;
import jakarta.validation.constraints.NotNull;

public record ActualizarDiscusionDTO(
        String titulo,
        String mensaje,
        Estado estado,
        @NotNull long temaId
){
}
