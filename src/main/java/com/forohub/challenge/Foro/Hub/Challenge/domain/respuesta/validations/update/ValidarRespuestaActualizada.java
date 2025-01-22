package com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.validations.update;


import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.dto.ActualizarRespuestaDTO;

public interface ValidarRespuestaActualizada {

    void validate(ActualizarRespuestaDTO data, Long respuestaId);
}
