package co.edu.udistrital.mdp.back.dto;

import java.util.List;

import lombok.Data;

@Data
public class PrendaDetailDTO extends PrendaDTO {
    private List<ColorDTO> colores;
    private List<OutfitDTO> outfits;
    private List<ImagenDTO> imagenes;
    private List<ComentarioDTO> comentarios;
}
