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

    // Muchos comentarios pueden pertenecer a un mismo outfit
    @ManyToOne
    @JoinColumn(name = "outfit_id")
    private OutfitEntity outfit;

    // Muchos comentarios pueden pertenecer a una misma prenda
    @ManyToOne
    @JoinColumn(name = "prenda_id")
    private PrendaEntity prenda;
}
