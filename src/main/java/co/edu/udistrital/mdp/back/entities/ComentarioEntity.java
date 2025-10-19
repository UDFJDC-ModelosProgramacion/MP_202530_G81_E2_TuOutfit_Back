package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Clase que representa un comentario en la persistencia
 */

@Data
@Entity
public class ComentarioEntity extends BaseEntity {
	private String texto;
    private double calificacion;

    // Muchos comentarios pueden pertenecer a un mismo usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;
}
