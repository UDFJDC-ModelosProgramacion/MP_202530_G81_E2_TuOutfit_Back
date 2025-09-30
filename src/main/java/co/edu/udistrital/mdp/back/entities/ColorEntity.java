package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un color en la persistencia.
 */

@Data
@Entity
public class ColorEntity extends BaseEntity {

    // Nombre del color (ej: "Rojo", "Azul", "Verde")
    private String nombre;

    // Un color puede estar asociado a muchas prendas
    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrendaEntity> prendas = new ArrayList<>();
}
