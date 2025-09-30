package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.List;

/**
 * Clase que representa una tienda en la persistencia
 */

@Data
@Entity
public class TiendaEntity extends BaseEntity {

    private String nombre;
    private String direccion;
    private String horario;
    private String ubicacion;

    @PodamExclude
    // Una tienda pertenece a una marca
    @ManyToOne
    @JoinColumn(name = "marca_id")
    private MarcaEntity marca;

    @PodamExclude
    // Una tienda puede tener muchas prendas
    @OneToMany(mappedBy = "tienda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrendaEntity> prendas;
}
