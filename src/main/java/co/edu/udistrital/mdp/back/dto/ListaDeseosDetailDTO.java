package co.edu.udistrital.mdp.back.dto;

import java.util.List;

import lombok.Data;

@Data
public class ListaDeseosDetailDTO extends ListaDeseosDTO {
    private List<OutfitDTO> outfits;
}
