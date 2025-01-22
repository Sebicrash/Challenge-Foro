package com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.validations.create;


import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Estado;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.repository.DiscusionRepository;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.dto.CrearRespuestaDTO;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespuestaDiscusionValida implements ValidarRespuestaCreada{

    @Autowired
    private DiscusionRepository repository;

    @Override
    public void validate(CrearRespuestaDTO data) {
        var discusionExiste = repository.existsById(data.temaId());

        if (!discusionExiste){
            throw new ValidationException("Esta discusion no existe.");
        }

        var discusionAbierto = repository.findById(data.temaId()).get().getEstado();

        if(discusionAbierto != Estado.OPEN){
            throw new ValidationException("Esta discusion no esta abierto.");
        }

    }
}