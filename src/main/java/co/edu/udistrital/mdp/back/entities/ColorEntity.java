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
@Table(name = "colores")
public class ColorEntity extends BaseEntity {

    /**
     * Nombre del color (ej: "Rojo", "Azul", "Verde")
     */
    @Column(nullable = false, unique = true)
    private String nombre;

    /**
     * Código hexadecimal del color (ej: "#FFFFFF")
     */
    @Column(name = "codigo_hex", nullable = false, unique = true, length = 20)
    private String codigoHex;

    /**
     * Un color puede estar asociado a muchas prendas
     */
    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrendaEntity> prendas = new ArrayList<>();
}

