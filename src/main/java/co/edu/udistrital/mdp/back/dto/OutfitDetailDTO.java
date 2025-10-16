package co.edu.udistrital.mdp.back.dto;

import java.util.ArrayList;

import java.util.List;

import lombok.Data;

@Data
public class OutfitDetailDTO extends OutfitDTO {
    private List<ListaDeseosDTO> listadeseos = new ArrayList<>();
    private List<RecomendacionDTO> recomendacion = new ArrayList<>();
}
