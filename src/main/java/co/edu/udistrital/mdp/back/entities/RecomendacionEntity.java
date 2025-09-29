package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class RecomendacionEntity extends BaseEntity {

    private String texto;
    private int calificacion;


    @ManyToOne
    private UsuarioEntity usuario;

    @ManyToOne
    private OutfitEntity outfit;
}