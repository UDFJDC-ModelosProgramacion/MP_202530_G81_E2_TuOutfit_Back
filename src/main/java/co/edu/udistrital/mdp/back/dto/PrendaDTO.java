package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class PrendaDTO {
    private String nombre;
    private Double precio;
    private ColorDetailDTO color;
    private MarcaDetailDTO marca;
    private OutfitDetailDTO outfit;
}
