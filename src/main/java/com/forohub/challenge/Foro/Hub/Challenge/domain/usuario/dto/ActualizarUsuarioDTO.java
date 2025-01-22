package com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.dto;


import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.Rol;

public record ActualizarUsuarioDTO(
        String password,
        Rol rol,
        String nombre,
        String apellido,
        String email,
        Boolean enabled
) {
}

