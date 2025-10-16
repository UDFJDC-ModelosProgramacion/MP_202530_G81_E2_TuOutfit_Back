package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.ImagenOutfitEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.ImagenOutfitRepository;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImagenOutfitService {

    @Autowired
    private ImagenOutfitRepository imagenRepository;

    @Autowired
    private OutfitRepository outfitRepository;

   
    @Transactional
    public ImagenOutfitEntity createImagen(ImagenOutfitEntity imagenEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de la imagen");

        if (imagenEntity.getImagen() == null || imagenEntity.getImagen().isBlank())
            throw new IllegalOperationException("El campo 'imagen' no puede ser nulo o vacío");

        if (imagenEntity.getOutfit() == null)
            throw new IllegalOperationException("Debe asociarse a un outfit válido");

        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(imagenEntity.getOutfit().getId());
        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        imagenEntity.setOutfit(outfitEntity.get());

        log.info("Termina proceso de creación de la imagen");
        return imagenRepository.save(imagenEntity);
    }

  
    @Transactional
    public List<ImagenOutfitEntity> getImagenes() {
        log.info("Inicia proceso de consultar todas las imágenes");
        return imagenRepository.findAll();
    }

  
    @Transactional
    public ImagenOutfitEntity getImagen(Long imagenId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la imagen con id = {}", imagenId);
        Optional<ImagenOutfitEntity> imagenEntity = imagenRepository.findById(imagenId);
        if (imagenEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);
        log.info("Termina proceso de consultar la imagen con id = {}", imagenId);
        return imagenEntity.get();
    }

  
    @Transactional
    public ImagenOutfitEntity updateImagen(Long imagenId, ImagenOutfitEntity imagen)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la imagen con id = {}", imagenId);
        Optional<ImagenOutfitEntity> imagenEntity = imagenRepository.findById(imagenId);
        if (imagenEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);

        if (imagen.getImagen() == null || imagen.getImagen().isBlank())
            throw new IllegalOperationException("El campo 'imagen' no puede ser nulo o vacío");

        imagen.setId(imagenId);
        log.info("Termina proceso de actualizar la imagen con id = {}", imagenId);
        return imagenRepository.save(imagen);
    }

   
    @Transactional
    public void deleteImagen(Long id) throws EntityNotFoundException, IllegalOperationException {
        Optional<ImagenOutfitEntity> imagen = imagenRepository.findById(id);
        if (imagen.isEmpty()) {
            throw new EntityNotFoundException("La imagen con id " + id + " no existe");
        }

        
        if (imagen.get().getOutfit() != null) {
            throw new IllegalOperationException("No se puede eliminar una imagen asociada a un outfit");
        }

        imagenRepository.delete(imagen.get());
    }
}