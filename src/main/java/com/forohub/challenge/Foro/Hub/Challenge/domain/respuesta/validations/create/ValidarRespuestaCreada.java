package com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.validations.create;


import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.dto.CrearRespuestaDTO;

public interface ValidarRespuestaCreada {
    void validate(CrearRespuestaDTO data);
}