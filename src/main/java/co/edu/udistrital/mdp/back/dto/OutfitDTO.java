package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class OutfitDTO {
    private Long id;
    private String nombre;
    private Double precioEstimado;
    private ImagenOutfitDTO imagen;
}
