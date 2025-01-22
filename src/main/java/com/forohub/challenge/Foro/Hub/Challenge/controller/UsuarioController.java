package com.forohub.challenge.Foro.Hub.Challenge.controller;


import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.Usuario;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.dto.ActualizarUsuarioDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.dto.CrearUsuarioDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.dto.DetallesUsuarioDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.repository.UsuarioRepository;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.validations.create.ValidarCrearUsuario;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.validations.update.ValidarActualizarUsuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    List<ValidarCrearUsuario> crearValidador;

    @Autowired
    List<ValidarActualizarUsuario> actualizarValidador;

    @PostMapping
    @Transactional
    public ResponseEntity<DetallesUsuarioDTO> crearUsuario(@RequestBody @Valid CrearUsuarioDTO crearUsuarioDTO,
                                                           UriComponentsBuilder uriBuilder) {
        crearValidador.forEach(v -> v.validate(crearUsuarioDTO));

        String hashedPassword = passwordEncoder.encode(crearUsuarioDTO.password());
        Usuario usuario = new Usuario(crearUsuarioDTO, hashedPassword);

        repository.save(usuario);
        var uri = uriBuilder.path("/usuarios/{username}").buildAndExpand(usuario.getUsername()).toUri();
        return ResponseEntity.created(uri).body(new DetallesUsuarioDTO(usuario));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<DetallesUsuarioDTO>> leerTodosUsuarios(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable) {
        var pagina = repository.findAll(pageable).map(DetallesUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    public ResponseEntity<Page<DetallesUsuarioDTO>> leerUsuariosActivos(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable) {
        var pagina = repository.findAllByEnabledTrue(pageable).map(DetallesUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<DetallesUsuarioDTO> leerUnUsuario(@PathVariable String username) {
        Usuario usuario = (Usuario) repository.findByUsername(username);
        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DetallesUsuarioDTO> leerUnUsuario(@PathVariable Long id) {
        Usuario usuario = repository.getReferenceById(id);
        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @PutMapping("/{username}")
    @Transactional
    public ResponseEntity<DetallesUsuarioDTO> actualizarUsuario(@RequestBody @Valid ActualizarUsuarioDTO actualizarUsuarioDTO, @PathVariable String username) {
        actualizarValidador.forEach(v -> v.validate(actualizarUsuarioDTO));

        Usuario usuario = (Usuario) repository.findByUsername(username);

        if (actualizarUsuarioDTO.password() != null) {
            String hashedPassword = passwordEncoder.encode(actualizarUsuarioDTO.password());
            usuario.actualizarUsuarioConPassword(actualizarUsuarioDTO, hashedPassword);

        } else {
            usuario.actualizarUsuario(actualizarUsuarioDTO);
        }

        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @DeleteMapping("/{username}")
    @Transactional
    public ResponseEntity<?> eliminarUsuario(@PathVariable String username) {
        Usuario usuario = (Usuario) repository.findByUsername(username);
        usuario.eliminarUsuario();
        return ResponseEntity.noContent().build();
    }
}