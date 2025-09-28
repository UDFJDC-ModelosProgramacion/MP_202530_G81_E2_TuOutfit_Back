package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class PrendaEntity extends BaseEntity {

    private String tipo;
    private String talla;
    private String color;
    private Double precioPromocion;

    //  (muchas prendas → 1 marca)
    @ManyToOne
    @JoinColumn(name = "marca_id")
    private MarcaEntity marca;

    // (1 prenda → muchas imágenes)
    //@OneToMany(mappedBy = "prenda", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<ImagenEntity> imagenes;

    //  (1 prenda → muchos comentarios)
    //@OneToMany(mappedBy = "prenda", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<ComentarioEntity> comentarios;

    //  (un outfit puede tener muchas prendas)
    //@ManyToMany(mappedBy = "prendas")
    //private List<OutfitEntity> outfits;
}
