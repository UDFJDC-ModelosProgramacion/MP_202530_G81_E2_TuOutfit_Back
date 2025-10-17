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
    private ImagenOutfitRepository imagenoutfitRepository;

    @Autowired
    private OutfitRepository outfitRepository;

   
    @Transactional
    public ImagenOutfitEntity createImagenOutfit(Long outfitId, ImagenOutfitEntity imagenoutfitEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de la imagen");

        if (imagenoutfitEntity.getImagen() == null || imagenoutfitEntity.getImagen().isBlank())
            throw new IllegalOperationException("El campo 'imagen' no puede ser nulo o vacío");

        if (imagenoutfitEntity.getOutfit() == null)
            throw new IllegalOperationException("Debe asociarse a un outfit válido");

        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(imagenoutfitEntity.getOutfit().getId());
        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        imagenoutfitEntity.setOutfit(outfitEntity.get());

        log.info("Termina proceso de creación de la imagen");
        return imagenoutfitRepository.save(imagenoutfitEntity);
    }

  
    @Transactional
    public List<ImagenOutfitEntity> getImagenOutfits(Long outfitId) {
        log.info("Inicia proceso de consultar todas las imágenes");
        return imagenoutfitRepository.findAll();
    }

  
    @Transactional
    public ImagenOutfitEntity getImagenOutfit( Long imagenoutfitId, Long imagenoutfitId2) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la imagen con id = {}", imagenoutfitId);
        Optional<ImagenOutfitEntity> imagenEntity = imagenoutfitRepository.findById(imagenoutfitId);
        if (imagenEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);
        log.info("Termina proceso de consultar la imagen con id = {}", imagenoutfitId);
        return imagenEntity.get();
    }

  
    @Transactional
    public ImagenOutfitEntity updateImagenOutfit( Long imagenoutfitId, Long imagenoutfitId2, ImagenOutfitEntity imagenoutfit)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la imagen con id = {}", imagenoutfitId);
        Optional<ImagenOutfitEntity> imagenEntity = imagenoutfitRepository.findById(imagenoutfitId);
        if (imagenEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);

        if (imagenoutfit.getImagen() == null || imagenoutfit.getImagen().isBlank())
            throw new IllegalOperationException("El campo 'imagen' no puede ser nulo o vacío");

        imagenoutfit.setId(imagenoutfitId);
        log.info("Termina proceso de actualizar la imagen con id = {}", imagenoutfitId);
        return imagenoutfitRepository.save(imagenoutfit);
    }

   
    @Transactional
    public void deleteImagenOutfit(Long id, Long imagenoutfitId) throws EntityNotFoundException, IllegalOperationException {
        Optional<ImagenOutfitEntity> imagenoutfit = imagenoutfitRepository.findById(id);
        if (imagenoutfit.isEmpty()) {
            throw new EntityNotFoundException("La imagen con id " + id + " no existe");
        }

        
        if (imagenoutfit.get().getOutfit() != null) {
            throw new IllegalOperationException("No se puede eliminar una imagen asociada a un outfit");
        }

        imagenoutfitRepository.delete(imagenoutfit.get());
    }
}