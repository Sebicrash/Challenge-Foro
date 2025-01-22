package com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.validations.update;


import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Estado;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.repository.DiscusionRepository;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.Respuesta;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.dto.ActualizarRespuestaDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.repository.RespuestaRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolucionDuplicada implements ValidarRespuestaActualizada {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private DiscusionRepository discusionRepository;

    @Override
    public void validate(ActualizarRespuestaDTO data, Long respuestaId) {
        if (data.solucion()){
            Respuesta respuesta = respuestaRepository.getReferenceById(respuestaId);
            var discusionResuelta = discusionRepository.getReferenceById(respuesta.getDiscusion().getId());
            if (discusionResuelta.getEstado() == Estado.CLOSED){
                throw new ValidationException("Esta discusion ya esta solucionado.");
            }
        }
    }
}