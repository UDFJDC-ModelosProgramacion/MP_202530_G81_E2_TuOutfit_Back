package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public class OutfitEntity extends BaseEntity {
    String nombre;
    Double precioEstimado;
}
