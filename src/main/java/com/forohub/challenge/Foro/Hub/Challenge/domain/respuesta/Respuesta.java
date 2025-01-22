package com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta;



import com.forohub.challenge.Foro.Hub.Challenge.domain.discusion.Discusion;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.dto.ActualizarRespuestaDTO;
import com.forohub.challenge.Foro.Hub.Challenge.domain.respuesta.dto.CrearRespuestaDTO;
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
@Table(name = "respuestas")
@Entity(name = "Respuesta")
@EqualsAndHashCode(of = "id")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;

    @Column(name="fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name="ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    private Boolean solucion;
    private Boolean borrado;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discusion_id")
    private Discusion discusion;

    public Respuesta(CrearRespuestaDTO crearRespuestaDTO, Usuario usuario, Discusion discusion) {
        this.mensaje = crearRespuestaDTO.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.ultimaActualizacion = LocalDateTime.now();
        this.solucion = false;
        this.borrado = false;
        this.usuario = usuario;
        this.discusion = discusion;
    }

    public void actualizarRespuesta(ActualizarRespuestaDTO actualizarRespuestaDTO){
        if(actualizarRespuestaDTO.mensaje() != null){
            this.mensaje = actualizarRespuestaDTO.mensaje();
        }
        if (actualizarRespuestaDTO.solucion() != null){
            this.solucion = actualizarRespuestaDTO.solucion();
        }
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void eliminarRespuesta(){
        this.borrado = true;
    }
}