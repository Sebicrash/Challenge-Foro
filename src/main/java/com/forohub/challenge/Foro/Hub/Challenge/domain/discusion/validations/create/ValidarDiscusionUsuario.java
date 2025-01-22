package com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.validations.create;

import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.dto.CrearDiscusionDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarDiscusionUsuario implements ValidarDiscusionCreada{

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validate(CrearDiscusionDTO data) {
        var existeUsuario = repository.existsById(data.usuarioId());
        if (!existeUsuario) {
            throw new ValidationException("Este usuario no existe");
        }

        var usuarioHabilitado = repository.findById(data.usuarioId()).get().getEnabled();
        if (!usuarioHabilitado) {
            throw new ValidationException("Este usuario fue deshabiliado.");
        }
    }
}