package co.edu.udistrital.mdp.back.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.back.dto.OcasionDTO;
import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.services.CategoriaOcasionService;

@RestController
@RequestMapping("/categorias/{categoriaId}/ocasiones")
public class CategoriaOcasionController {

    @Autowired
    private CategoriaOcasionService categoriaOcasionService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia una ocasión existente a una categoría.
     */
    @PostMapping("/{ocasionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public OcasionDTO addOcasionToCategoria(@PathVariable Long categoriaId, @PathVariable Long ocasionId)
            throws EntityNotFoundException {
        OcasionEntity ocasionEntity = categoriaOcasionService.addOcasionToCategoria(categoriaId, ocasionId);
        return modelMapper.map(ocasionEntity, OcasionDTO.class);
    }

    /**
     * Obtiene todas las ocasiones asociadas a una categoría.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<OcasionDTO> getOcasionesByCategoria(@PathVariable Long categoriaId) throws EntityNotFoundException {
        List<OcasionEntity> ocasiones = categoriaOcasionService.getOcasionesByCategoria(categoriaId);
        return modelMapper.map(ocasiones, new TypeToken<List<OcasionDTO>>() {}.getType());
    }

    /**
     * Obtiene una ocasión específica asociada a una categoría.
     */
    @GetMapping("/{ocasionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public OcasionDTO getOcasionByCategoria(@PathVariable Long categoriaId, @PathVariable Long ocasionId)
            throws EntityNotFoundException {
        OcasionEntity ocasionEntity = categoriaOcasionService.getOcasionByCategoria(categoriaId, ocasionId);
        return modelMapper.map(ocasionEntity, OcasionDTO.class);
    }

    /**
     * Elimina la asociación entre una ocasión y una categoría.
     */
    @DeleteMapping("/{ocasionId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeOcasionFromCategoria(@PathVariable Long categoriaId, @PathVariable Long ocasionId)
            throws EntityNotFoundException {
        categoriaOcasionService.removeOcasionFromCategoria(categoriaId, ocasionId);
    }
}
