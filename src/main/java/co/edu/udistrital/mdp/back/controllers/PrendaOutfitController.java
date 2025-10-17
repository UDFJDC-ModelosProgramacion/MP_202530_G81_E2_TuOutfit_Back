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

import co.edu.udistrital.mdp.back.dto.OutfitDTO;
import co.edu.udistrital.mdp.back.dto.OutfitDetailDTO;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.PrendaOutfitService;

@RestController
@RequestMapping("/prendas")
public class PrendaOutfitController {
    @Autowired
	private PrendaOutfitService prendaOutfitService;

	@Autowired
	private ModelMapper modelMapper;


    @GetMapping(value = "/{prendaId}/outfits/{outfitId}")
	@ResponseStatus(code = HttpStatus.OK)
	public OutfitDetailDTO getOutfit(@PathVariable Long prendaId, @PathVariable Long outfitId)
			throws EntityNotFoundException, IllegalOperationException {
		OutfitEntity outfitEntity = prendaOutfitService.getOutfit(prendaId, outfitId);
		return modelMapper.map(outfitEntity, OutfitDetailDTO.class);
	}


    @GetMapping(value = "/{prendaId}/outfits")
	@ResponseStatus(code = HttpStatus.OK)
	public List<OutfitDetailDTO> getOutfits(@PathVariable Long prendaId) throws EntityNotFoundException {
		List<OutfitEntity> outfitEntity = prendaOutfitService.getOutfits(prendaId);
		return modelMapper.map(outfitEntity, new TypeToken<List<OutfitDetailDTO>>() {
		}.getType());
	}


    @PostMapping(value = "/{prendaId}/outfits/{outfitId}")
	@ResponseStatus(code = HttpStatus.OK)
	public OutfitDetailDTO addOutfit(@PathVariable Long prendaId, @PathVariable Long outfitId)
			throws EntityNotFoundException {
		OutfitEntity outfitEntity = prendaOutfitService.addOutfit(prendaId, outfitId);
		return modelMapper.map(outfitEntity, OutfitDetailDTO.class);
	}


    @PutMapping(value = "/{prendaId}/outfits")
	@ResponseStatus(code = HttpStatus.OK)
	public List<OutfitDetailDTO> replaceOutfits(@PathVariable Long prendaId, @RequestBody List<OutfitDTO> outfits)
			throws EntityNotFoundException {
		List<OutfitEntity> entities = modelMapper.map(outfits, new TypeToken<List<OutfitEntity>>() {
		}.getType());
		List<OutfitEntity> outfitsList = prendaOutfitService.addOutfits(prendaId, entities);
		return modelMapper.map(outfitsList, new TypeToken<List<OutfitDetailDTO>>() {
		}.getType());

	}


    @DeleteMapping(value = "/{prendaId}/outfits/{outfitId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeOutfit(@PathVariable Long prendaId, @PathVariable Long outfitId)
			throws EntityNotFoundException {
		prendaOutfitService.removeOutfit(prendaId, outfitId);
	}
}
