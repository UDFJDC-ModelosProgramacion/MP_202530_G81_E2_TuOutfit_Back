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

import co.edu.udistrital.mdp.back.dto.PrendaDTO;
import co.edu.udistrital.mdp.back.dto.PrendaDetailDTO;
import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.services.PrendaService;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

@RestController
@RequestMapping("/api/prendas")
public class PrendaController {

    @Autowired
    private PrendaService prendaService;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PrendaDTO create(@RequestBody PrendaDTO prendaDTO) throws IllegalOperationException, EntityNotFoundException {
 
        PrendaEntity prendaToCreate = modelMapper.map(prendaDTO, PrendaEntity.class);

        PrendaEntity prendaCreadaEntity = prendaService.createPrenda(prendaToCreate);

        return modelMapper.map(prendaCreadaEntity, PrendaDTO.class);
    }


    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PrendaDTO> findAll() {

        List<PrendaEntity> prendas = prendaService.getAllPrendas(); 
        
        return modelMapper.map(prendas, new TypeToken<List<PrendaDTO>>() {
        }.getType());
    }


    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PrendaDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {

        PrendaEntity prendaEntity = prendaService.getPrendaById(id);
        
        return modelMapper.map(prendaEntity, PrendaDetailDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PrendaDTO update(@PathVariable Long id, @RequestBody PrendaDTO prendaDTO)
            throws EntityNotFoundException, IllegalOperationException {
        
        PrendaEntity prendaDetailsEntity = modelMapper.map(prendaDTO, PrendaEntity.class);
        
    
        PrendaEntity prendaActualizadaEntity = prendaService.updatePrenda(id, prendaDetailsEntity);
        
        return modelMapper.map(prendaActualizadaEntity, PrendaDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {

        prendaService.deletePrenda(id);
    }
}