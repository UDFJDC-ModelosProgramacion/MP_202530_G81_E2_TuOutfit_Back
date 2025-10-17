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
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.OutfitPrendaService;

@RestController
@RequestMapping("/outfits")
public class OutfitPrendaController {
@Autowired
	private OutfitPrendaService outfitPrendaService;

	@Autowired
	private ModelMapper modelMapper;

    @PostMapping(value = "/{outfitId}/prendas/{prendaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public PrendaDetailDTO addPrenda(@PathVariable Long prendaId, @PathVariable Long outfitId)
			throws EntityNotFoundException {
		PrendaEntity prendaEntity = outfitPrendaService.addPrenda(outfitId, prendaId);
		return modelMapper.map(prendaEntity, PrendaDetailDTO.class);
	}


    @GetMapping(value = "/{outfitId}/prendas/{prendaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public PrendaDetailDTO getPrenda(@PathVariable Long prendaId, @PathVariable Long outfitId)
			throws EntityNotFoundException, IllegalOperationException {
		PrendaEntity prendaEntity = outfitPrendaService.getPrenda(outfitId, prendaId);
		return modelMapper.map(prendaEntity, PrendaDetailDTO.class);
	}


    @PutMapping(value = "/{outfitId}/prendas")
	@ResponseStatus(code = HttpStatus.OK)
	public List<PrendaDetailDTO> addPrendas(@PathVariable Long outfitId, @RequestBody List<PrendaDTO> prendas)
			throws EntityNotFoundException {
		List<PrendaEntity> entities = modelMapper.map(prendas, new TypeToken<List<PrendaEntity>>() {
		}.getType());
		List<PrendaEntity> prendasList = outfitPrendaService.replacePrendas(outfitId, entities);
		return modelMapper.map(prendasList, new TypeToken<List<PrendaDetailDTO>>() {
		}.getType());
	}


    @GetMapping(value = "/{outfitId}/prendas")
	@ResponseStatus(code = HttpStatus.OK)
	public List<PrendaDetailDTO> getPrendas(@PathVariable Long outfitId) throws EntityNotFoundException {
		List<PrendaEntity> prendaEntity = outfitPrendaService.getPrendas(outfitId);
		return modelMapper.map(prendaEntity, new TypeToken<List<PrendaDetailDTO>>() {
		}.getType());
	}


    @DeleteMapping(value = "/{outfitId}/prendas/{prendaId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removePrenda(@PathVariable Long prendaId, @PathVariable Long outfitId)
			throws EntityNotFoundException {
		outfitPrendaService.removePrenda(outfitId, prendaId);
	}
}
