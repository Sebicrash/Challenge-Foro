package com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearDiscusionDTO(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotNull Long usuarioId
) {
}