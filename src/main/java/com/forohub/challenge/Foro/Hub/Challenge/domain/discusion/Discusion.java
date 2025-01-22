package com.forohub.challenge.Foro.Hub.Challenge.domain.discusion;


import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.dto.ActualizarDiscusionDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.dto.CrearDiscusionDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "topicos")
@Entity(name = "Discusion")
@EqualsAndHashCode(of = "id")
public class Discusion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;

    @Column(name="fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name="ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @Enumerated(EnumType.STRING)
    private Estado estado;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    public Discusion(CrearDiscusionDTO crearDiscusionDTO, Usuario usuario) {
        this.titulo = crearDiscusionDTO.titulo();
        this.mensaje = crearDiscusionDTO.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.ultimaActualizacion = LocalDateTime.now();
        this.estado = Estado.OPEN;
        this.usuario = usuario;
    }


    public void actualizarDiscusion(ActualizarDiscusionDTO actualizarDiscusionDTO){
        if (actualizarDiscusionDTO.titulo() != null){
            this.titulo = actualizarDiscusionDTO.titulo();
        }
        if (actualizarDiscusionDTO.mensaje() != null){
            this.mensaje = actualizarDiscusionDTO.mensaje();
        }
        if(actualizarDiscusionDTO.estado() != null){
            this.estado = actualizarDiscusionDTO.estado();
        }
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void eliminarDiscusion(){

        this.estado = Estado.DELETED;
    }

    public void setEstado(Estado estado){

        this.estado = estado;
    }
}