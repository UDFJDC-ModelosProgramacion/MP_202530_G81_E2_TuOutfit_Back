package co.edu.udistrital.mdp.back.dto;

import java.util.ArrayList;

import lombok.Data;

import java.util.List;

@Data
public class CategoriaDetailDTO extends CategoriaDTO {
    private List<OutfitDTO> outfit = new ArrayList<>();
    private List<OcasionDTO> ocasion = new ArrayList<>();
}
