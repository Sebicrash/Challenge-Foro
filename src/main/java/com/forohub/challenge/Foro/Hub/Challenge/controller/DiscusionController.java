package com.forohub.challenge.Foro.Hub.Challenge.controller;


import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Discusion;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Estado;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.dto.ActualizarDiscusionDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.dto.CrearDiscusionDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.dto.DetallesDiscusionDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.repository.DiscusionRepository;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.validations.create.ValidarDiscusionCreada;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.validations.update.ValidarDiscusionActualizada;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.Respuesta;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.dto.DetalleRespuestaDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.repository.RespuestaRepository;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.Usuario;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/discusiones")
public class DiscusionController {

    @Autowired
    private DiscusionRepository discusionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    RespuestaRepository respuestaRepository;

    @Autowired
    List<ValidarDiscusionCreada> crearValidadores;

    @Autowired
    List<ValidarDiscusionActualizada> actualizarValidadores;

    @PostMapping
    @Transactional
    public ResponseEntity<DetallesDiscusionDTO> crearDiscusion(@RequestBody @Valid CrearDiscusionDTO crearDiscusionDTO,
                                                               UriComponentsBuilder uriBuilder) {
        crearValidadores.forEach(v -> v.validate(crearDiscusionDTO));

        Usuario usuario = usuarioRepository.findById(crearDiscusionDTO.usuarioId()).get();
        Discusion discusion = new Discusion(crearDiscusionDTO, usuario);

        discusionRepository.save(discusion);

        var uri = uriBuilder.path("/discusiones/{id}").buildAndExpand(discusion.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetallesDiscusionDTO(discusion));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<DetallesDiscusionDTO>> leerDiscusiones(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.DESC) Pageable pageable) {
        var pagina = discusionRepository.findAll(pageable).map(DetallesDiscusionDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    public ResponseEntity<Page<DetallesDiscusionDTO>> leerDiscusionesNoEliminadas(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.DESC) Pageable pageable) {
        var pagina = discusionRepository.findAllByEstadoIsNot(Estado.DELETED, pageable).map(DetallesDiscusionDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallesDiscusionDTO> leerUnaDiscusion(@PathVariable Long id) {
        Discusion discusion = discusionRepository.getReferenceById(id);
        var datosDiscusion = new DetallesDiscusionDTO(
                discusion.getId(),
                discusion.getTitulo(),
                discusion.getMensaje(),
                discusion.getFechaCreacion(),
                discusion.getUltimaActualizacion(),
                discusion.getEstado(),
                discusion.getUsuario().getUsername()
        );
        return ResponseEntity.ok(datosDiscusion);
    }

    @GetMapping("/{id}/solucion")
    public ResponseEntity<DetalleRespuestaDTO> leerSolucionDiscusion(@PathVariable Long id) {
        Respuesta respuesta = respuestaRepository.getReferenceByDiscusionId(id);

        var datosRespuesta = new DetalleRespuestaDTO(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getUltimaActualizacion(),
                respuesta.getSolucion(),
                respuesta.getBorrado(),
                respuesta.getUsuario().getId(),
                respuesta.getUsuario().getUsername(),
                respuesta.getDiscusion().getId(),
                respuesta.getDiscusion().getTitulo()
        );
        return ResponseEntity.ok(datosRespuesta);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DetallesDiscusionDTO> actualizarDiscusion(@RequestBody @Valid ActualizarDiscusionDTO actualizarDiscusionDTO, @PathVariable Long id) {
        actualizarValidadores.forEach(v -> v.validate(actualizarDiscusionDTO));

        Discusion discusion = discusionRepository.getReferenceById(id);

        if (actualizarDiscusionDTO.temaId() != null) {
            Discusion discusion1 = discusionRepository.getReferenceById(actualizarDiscusionDTO.temaId());
            discusion.actualizarDiscusion(actualizarDiscusionDTO);
        } else {
            discusion.actualizarDiscusion(actualizarDiscusionDTO);
        }

        var datosDiscusion = new DetallesDiscusionDTO(
                discusion.getId(),
                discusion.getTitulo(),
                discusion.getMensaje(),
                discusion.getFechaCreacion(),
                discusion.getUltimaActualizacion(),
                discusion.getEstado(),
                discusion.getUsuario().getUsername()
        );
        return ResponseEntity.ok(datosDiscusion);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarDiscusion(@PathVariable Long id) {
        Discusion discusion = discusionRepository.getReferenceById(id);
        discusion.eliminarDiscusion();
        return ResponseEntity.noContent().build();
    }
}