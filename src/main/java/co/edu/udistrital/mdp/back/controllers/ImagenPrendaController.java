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

import co.edu.udistrital.mdp.back.dto.ImagenPrendaDTO;
import co.edu.udistrital.mdp.back.entities.ImagenPrendaEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.ImagenPrendaService;


@RestController

@RequestMapping("/prendas")
public class ImagenPrendaController {
@Autowired

	private ImagenPrendaService imagenprendaService;

	@Autowired

	private ModelMapper modelMapper;

    @PostMapping(value = "/{prendaId}/imagenprendas")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ImagenPrendaDTO createImagenPrenda(@PathVariable Long prendaId, @RequestBody ImagenPrendaDTO imagenprenda)
			throws EntityNotFoundException, IllegalOperationException{
		ImagenPrendaEntity imagenprendaEnity = modelMapper.map(imagenprenda, ImagenPrendaEntity.class);
		ImagenPrendaEntity newImagenPrenda = imagenprendaService.createImagenPrenda(prendaId, imagenprendaEnity);
		return modelMapper.map(newImagenPrenda, ImagenPrendaDTO.class);
	}

    @GetMapping(value = "/{prendaId}/imagenprendas")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ImagenPrendaDTO> getImagenPrendas(@PathVariable Long prendaId)  {
		List<ImagenPrendaEntity> imagenprendas = imagenprendaService.getImagenPrendas(prendaId);
		return modelMapper.map(imagenprendas, new TypeToken<List<ImagenPrendaDTO>>() {
		}.getType());
	}

    @GetMapping(value = "/{prendaId}/imagenprendas/{imagenprendaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ImagenPrendaDTO getImagenPrenda(@PathVariable Long prendaId, @PathVariable Long imagenprendaId)
			throws EntityNotFoundException {
		ImagenPrendaEntity entity = imagenprendaService.getImagenPrenda(prendaId, imagenprendaId);
		return modelMapper.map(entity, ImagenPrendaDTO.class);
	}

    @PutMapping(value = "/{prendaId}/imagenprendas/{imagenprendasId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ImagenPrendaDTO updateImagenPrenda(@PathVariable Long prendaId, @PathVariable("imagenprendasId") Long imagenprendaId,
			@RequestBody ImagenPrendaDTO imagenprenda) throws EntityNotFoundException, IllegalOperationException {
		ImagenPrendaEntity imagenprendaEntity = modelMapper.map(imagenprenda, ImagenPrendaEntity.class);
		ImagenPrendaEntity newEntity = imagenprendaService.updateImagenPrenda(prendaId, imagenprendaId, imagenprendaEntity);
		return modelMapper.map(newEntity, ImagenPrendaDTO.class);
	}

    @DeleteMapping(value = "/{prendaId}/imagenprendas/{imagenprendaId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteImagenPrenda(@PathVariable Long prendaId, @PathVariable Long imagenprendaId)
			throws EntityNotFoundException, IllegalOperationException {
		imagenprendaService.deleteImagenPrenda(prendaId, imagenprendaId);
	}
}
