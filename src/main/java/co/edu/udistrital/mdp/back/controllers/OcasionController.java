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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.back.dto.OcasionDTO;
import co.edu.udistrital.mdp.back.dto.OcasionDetailDTO;
import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.OcasionService;

@RestController
@RequestMapping("/ocasiones")
public class OcasionController {

    @Autowired
    private OcasionService ocasionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<OcasionDetailDTO> findAll() {
        List<OcasionEntity> ocasiones = ocasionService.getOcasiones();
        return modelMapper.map(ocasiones, new TypeToken<List<OcasionDetailDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public OcasionDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        OcasionEntity ocasionEntity = ocasionService.getOcasion(id);
        return modelMapper.map(ocasionEntity, OcasionDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public OcasionDTO create(@RequestBody OcasionDTO ocasionDTO) throws IllegalOperationException {
        OcasionEntity ocasionEntity = ocasionService.createOcasion(modelMapper.map(ocasionDTO, OcasionEntity.class));
        return modelMapper.map(ocasionEntity, OcasionDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public OcasionDTO update(@PathVariable Long id, @RequestBody OcasionDTO ocasionDTO)
            throws EntityNotFoundException, IllegalOperationException {
        OcasionEntity ocasionEntity = ocasionService.updateOcasion(id, modelMapper.map(ocasionDTO, OcasionEntity.class));
        return modelMapper.map(ocasionEntity, OcasionDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        ocasionService.deleteOcasion(id);
    }
}
