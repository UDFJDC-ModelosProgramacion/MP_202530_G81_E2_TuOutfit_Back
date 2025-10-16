package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class ImagenDTO {
    private Long id;
    private String imagen;
    private OutfitDTO outfit;
    private PrendaDTO prenda;
    private MarcaDTO marca;
}
