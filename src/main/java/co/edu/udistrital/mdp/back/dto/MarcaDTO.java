package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class MarcaDTO {
    private Long id;
    private String nombre;
    private String logo;
    private String ubicacion;
    private ImagenDTO imagen;
}
