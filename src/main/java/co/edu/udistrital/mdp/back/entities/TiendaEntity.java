package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class TiendaEntity extends BaseEntity {

    private String direccion;
    private String horario;

    @Lob
    private byte[] ubicacion;

    // 🔗 Relación con Marca (muchas tiendas → 1 marca)
    @ManyToOne
    @JoinColumn(name = "marca_id")
    private MarcaEntity marca;
}
