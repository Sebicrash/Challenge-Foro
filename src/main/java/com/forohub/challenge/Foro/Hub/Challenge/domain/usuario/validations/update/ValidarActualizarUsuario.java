package com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.validations.update;

import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.dto.ActualizarUsuarioDTO;

public interface ValidarActualizarUsuario {
    void validate(ActualizarUsuarioDTO data);
}