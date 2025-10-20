package co.edu.udistrital.mdp.back.controllers;

import java.util.List;
import org.modelmapper.ModelMapper;
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

import co.edu.udistrital.mdp.back.dto.OutfitDTO;
import co.edu.udistrital.mdp.back.dto.OutfitDetailDTO;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.OutfitService;
import org.modelmapper.TypeToken;

@RestController

@RequestMapping("/outfits")
public class OutfitController {
    @Autowired
	private OutfitService outfitService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)

	public List<OutfitDetailDTO> findAll() {
		List<OutfitEntity> outfits = outfitService.getOutfits();
		return modelMapper.map(outfits, new TypeToken<List<OutfitDetailDTO>>() {
		}.getType());
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)

	public OutfitDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		OutfitEntity outfitEntity = outfitService.getOutfit(id);
		return modelMapper.map(outfitEntity, OutfitDetailDTO.class);
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public OutfitDTO create(@RequestBody OutfitDTO outfitDTO) throws IllegalOperationException, EntityNotFoundException {
		OutfitEntity outfitEntity = outfitService.createOutfit(modelMapper.map(outfitDTO, OutfitEntity.class));
		return modelMapper.map(outfitEntity, OutfitDTO.class);
	}

	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public OutfitDTO update(@PathVariable Long id, @RequestBody OutfitDTO outfitDTO)
				throws EntityNotFoundException, IllegalOperationException {
		OutfitEntity outfitEntity = outfitService.updateOutfit(id, modelMapper.map(outfitDTO, OutfitEntity.class));
		return modelMapper.map(outfitEntity, OutfitDTO.class);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) throws EntityNotFoundException {
		outfitService.deleteOutfit(id);
	}
}
