package com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.dto;


import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.Rol;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.Usuario;

public record DetallesUsuarioDTO(
        Long id,
        String username,
        Rol rol,
        String nombre,
        String apellido,
        String email,
        Boolean enabled
) {

    public DetallesUsuarioDTO(Usuario usuario){
        this(usuario.getId(),
                usuario.getUsername(),
                usuario.getRol(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
    }
}