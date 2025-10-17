package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class ImagenPrendaDTO {
    private String imagen;
    private PrendaDTO prenda;
    private MarcaDTO marca;
}
