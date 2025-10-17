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

import co.edu.udistrital.mdp.back.dto.ImagenOutfitDTO;
import co.edu.udistrital.mdp.back.entities.ImagenOutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.ImagenOutfitService;


@RestController

@RequestMapping("/outfits")
public class ImagenOutfitController {
@Autowired

	private ImagenOutfitService imagenoutfitService;

	@Autowired

	private ModelMapper modelMapper;

    @PostMapping(value = "/{outfitId}/imagenoutfits")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ImagenOutfitDTO createImagenOutfit(@PathVariable Long outfitId, @RequestBody ImagenOutfitDTO imagenoutfit)
			throws EntityNotFoundException, IllegalOperationException {
		ImagenOutfitEntity imagenoutfitEnity = modelMapper.map(imagenoutfit, ImagenOutfitEntity.class);
		ImagenOutfitEntity newImagenOutfit = imagenoutfitService.createImagenOutfit(outfitId, imagenoutfitEnity);
		return modelMapper.map(newImagenOutfit, ImagenOutfitDTO.class);
	}

    @GetMapping(value = "/{outfitId}/imagenoutfits")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ImagenOutfitDTO> getImagenOutfits(@PathVariable Long outfitId) throws EntityNotFoundException {
		List<ImagenOutfitEntity> imagenoutfits = imagenoutfitService.getImagenOutfits(outfitId);
		return modelMapper.map(imagenoutfits, new TypeToken<List<ImagenOutfitDTO>>() {
		}.getType());
	}

    @GetMapping(value = "/{outfitId}/imagenoutfits/{imagenoutfitId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ImagenOutfitDTO getImagenOutfit(@PathVariable Long outfitId, @PathVariable Long imagenoutfitId)
			throws EntityNotFoundException {
		ImagenOutfitEntity entity = imagenoutfitService.getImagenOutfit(outfitId, imagenoutfitId);
		return modelMapper.map(entity, ImagenOutfitDTO.class);
	}

    @PutMapping(value = "/{outfitId}/imagenoutfits/{imagenoutfitId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ImagenOutfitDTO updateImagenOutfit(@PathVariable Long outfitId, @PathVariable("imagenoutfitsId") Long imagenoutfitId,
			@RequestBody ImagenOutfitDTO imagenoutfit) throws EntityNotFoundException, IllegalOperationException {
		ImagenOutfitEntity imagenoutfitEntity = modelMapper.map(imagenoutfit, ImagenOutfitEntity.class);
		ImagenOutfitEntity newEntity = imagenoutfitService.updateImagenOutfit(outfitId, imagenoutfitId, imagenoutfitEntity);
		return modelMapper.map(newEntity, ImagenOutfitDTO.class);
	}

    @DeleteMapping(value = "/{outfitId}/imagenoutfits/{imagenoutfitId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteImagenOutfit(@PathVariable Long outfitId, @PathVariable Long imagenoutfitId)
			throws EntityNotFoundException, IllegalOperationException {
		imagenoutfitService.deleteImagenOutfit(outfitId, imagenoutfitId);
	}
}
