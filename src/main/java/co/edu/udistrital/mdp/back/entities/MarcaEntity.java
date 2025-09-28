package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class MarcaEntity extends BaseEntity {

    private String nombre;
    private String sitioOficial;

    @Lob
    private byte[] logo;

    // 🔗 Relación con Prenda (1 marca → muchas prendas)
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrendaEntity> prendas;

    // 🔗 Relación con Tienda (1 marca → muchas tiendas)
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TiendaEntity> tiendas;
}
