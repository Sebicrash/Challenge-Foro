package com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.validations.create;

import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.dto.CrearDiscusionDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.repository.DiscusionRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscusionDuplicada implements ValidarDiscusionCreada{

    @Autowired
    private DiscusionRepository discusionRepository;


    @Override
    public void validate(CrearDiscusionDTO data) {
        var discusionDuplicada = discusionRepository.existsByTituloAndMensaje(data.titulo(), data.mensaje());
        if(discusionDuplicada){
            throw new ValidationException("Este topico ya existe. Revisa /topicos/" + discusionRepository.findByTitulo(data.titulo()).getId());

        }
    }
}
