package co.edu.udistrital.mdp.back.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.back.dto.OcasionDetailDTO;
import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.services.CategoriaOcasionService;

/**
 * Clase que implementa el recurso "categorias/{id}/ocasiones".
 * 
 * Permite gestionar las asociaciones entre una categoría y sus ocasiones.
 * 
 * @author 
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaOcasionController {

    @Autowired
    private CategoriaOcasionService categoriaOcasionService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca y devuelve todas las ocasiones asociadas a una categoría.
     *
     * @param categoriaId El ID de la categoría.
     * @return Lista de {@link OcasionDetailDTO} con las ocasiones asociadas.
     */
    @GetMapping(value = "/{categoriaId}/ocasiones")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OcasionDetailDTO> getOcasionesPorCategoria(@PathVariable Long categoriaId)
            throws EntityNotFoundException {
        List<OcasionEntity> ocasiones = categoriaOcasionService.getOcasionesPorCategoria(categoriaId);
        return modelMapper.map(ocasiones, new TypeToken<List<OcasionDetailDTO>>() {
        }.getType());
    }

    /**
     * Asocia una ocasión existente a una categoría existente.
     *
     * @param categoriaId El ID de la categoría.
     * @param ocasionId   El ID de la ocasión que se va a asociar.
     * @return {@link OcasionDetailDTO} con la ocasión asociada.
     */
    @PostMapping(value = "/{categoriaId}/ocasiones/{ocasionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public OcasionDetailDTO addOcasionACategoria(@PathVariable Long categoriaId, @PathVariable Long ocasionId)
            throws EntityNotFoundException {
        OcasionEntity ocasion = categoriaOcasionService.addOcasionACategoria(categoriaId, ocasionId);
        return modelMapper.map(ocasion, OcasionDetailDTO.class);
    }

    /**
     * Elimina la relación entre una categoría y una ocasión específica.
     *
     * @param categoriaId El ID de la categoría.
     * @param ocasionId   El ID de la ocasión que se desea eliminar de la categoría.
     */
    @DeleteMapping(value = "/{categoriaId}/ocasiones/{ocasionId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeOcasionDeCategoria(@PathVariable Long categoriaId, @PathVariable Long ocasionId)
            throws EntityNotFoundException {
        categoriaOcasionService.removeOcasionDeCategoria(categoriaId, ocasionId);
    }
}

