package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.List;

/**
 * Clase que representa una marca en la persistencia
 */

@Data
@Entity
public class MarcaEntity extends BaseEntity {

    private String nombre;
    private String logo;
    private String ubicacion;

    @PodamExclude
    // Una marca puede tener muchas prendas
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrendaEntity> prendas;

    @PodamExclude
    // Una marca puede tener muchas tiendas
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TiendaEntity> tiendas;

    @PodamExclude
    // Una marca tiene una sola imagen (ej: logo)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imagen_id")
    private ImagenPrendaEntity imagen;
}
