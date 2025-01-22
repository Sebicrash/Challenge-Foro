package com.forohub.challenge.Foro.Hub.Challenge.controller;


import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Discusion;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Estado;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.repository.DiscusionRepository;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.Respuesta;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.dto.ActualizarRespuestaDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.dto.CrearRespuestaDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.dto.DetalleRespuestaDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.repository.RespuestaRepository;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.validations.create.ValidarRespuestaCreada;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.validations.update.ValidarRespuestaActualizada;
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
@RequestMapping("/respuestas")
public class RespuestaController {
    @Autowired
    private DiscusionRepository discusionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    List<ValidarRespuestaCreada> crearValidadores;

    @Autowired
    List<ValidarRespuestaActualizada> actualizarValidadores;

    @PostMapping
    @Transactional
    public ResponseEntity<DetalleRespuestaDTO> crearRespuesta(@RequestBody @Valid CrearRespuestaDTO crearRespuestaDTO,
                                                              UriComponentsBuilder uriBuilder){
        crearValidadores.forEach(v -> v.validate(crearRespuestaDTO));

        Usuario usuario = usuarioRepository.getReferenceById(crearRespuestaDTO.usuarioId());
        Discusion discusion = discusionRepository.findById(crearRespuestaDTO.temaId()).get();

        var respuesta = new Respuesta(crearRespuestaDTO, usuario, discusion);
        respuestaRepository.save(respuesta);

        var uri = uriBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalleRespuestaDTO(respuesta));

    }

    @GetMapping("/topico/{temaId}")
    public ResponseEntity<Page<DetalleRespuestaDTO>>
    leerRespuestaDeTopico(@PageableDefault(size = 5, sort = {"ultimaActualizacion"},
            direction = Sort.Direction.ASC) Pageable pageable, @PathVariable Long discusionId){
        var pagina = respuestaRepository.findAllByDiscusionId(discusionId, pageable).map(DetalleRespuestaDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/usuario/{nombreUsuario}")
    public ResponseEntity<Page<DetalleRespuestaDTO>>
    leerRespuestasDeUsuarios(@PageableDefault(size = 5, sort = {"ultimaActualizacion"},
            direction = Sort.Direction.ASC)Pageable pageable, @PathVariable Long usuarioId){
        var pagina = respuestaRepository.findAllByUsuarioId(usuarioId, pageable).map(DetalleRespuestaDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleRespuestaDTO> leerUnaRespuesta(@PathVariable Long id){
        Respuesta respuesta = respuestaRepository.getReferenceById(id);

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
    public ResponseEntity<DetalleRespuestaDTO> actualizarRespuesta(@RequestBody @Valid ActualizarRespuestaDTO actualizarRespuestaDTO, @PathVariable Long id){
        actualizarValidadores.forEach(v -> v.validate(actualizarRespuestaDTO, id));
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.actualizarRespuesta(actualizarRespuestaDTO);

        if(actualizarRespuestaDTO.solucion()){
            var temaResuelto = discusionRepository.getReferenceById(respuesta.getDiscusion().getId());
            temaResuelto.setEstado(Estado.CLOSED);
        }

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

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> borrarRespuesta(@PathVariable Long id){
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.eliminarRespuesta();
        return ResponseEntity.noContent().build();
    }
}