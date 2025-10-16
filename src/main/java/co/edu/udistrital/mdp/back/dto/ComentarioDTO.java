package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class ComentarioDTO {
    private Long id;
    private Long usuarioId;
    private String texto;
    private double calificacion;
}
