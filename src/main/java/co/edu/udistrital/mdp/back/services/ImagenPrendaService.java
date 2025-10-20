package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.ImagenPrendaEntity;
import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.repositories.PrendaRepository;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.ImagenPrendaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImagenPrendaService {

    @Autowired
    ImagenPrendaRepository imagenPrendaRepository;
    @Autowired
    PrendaRepository prendaRepository;


    /**
     * Crea una nueva ImagenPrenda.
     * @param prendaId 
     */
   @Transactional
public ImagenPrendaEntity createImagenPrenda(Long prendaId, ImagenPrendaEntity imagenPrendaEntity)
        throws EntityNotFoundException, IllegalOperationException {

    log.info("Inicia proceso de creación de una imagen para la prenda con id = {}", prendaId);

   
    Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
    if (prendaEntity.isEmpty()) {
        throw new EntityNotFoundException("La prenda con el id proporcionado no existe");
    }

   
    if (imagenPrendaEntity.getImagen() == null || imagenPrendaEntity.getImagen().isBlank()) {
        throw new IllegalOperationException("El campo 'imagen' no puede ser nulo o vacío");
    }

    
    imagenPrendaEntity.setPrenda(prendaEntity.get());

    
    ImagenPrendaEntity nuevaImagen = imagenPrendaRepository.save(imagenPrendaEntity);

    log.info("Termina proceso de creación de una imagen para la prenda con id = {}", prendaId);
    return nuevaImagen;
}
    /**
     * Obtiene todas las imágenes de prenda.
     * @param prendaId 
     */
    @Transactional
    public List<ImagenPrendaEntity> getImagenPrendas(Long prendaId) {
        log.info("Inicia proceso de consultar todas las imágenes de prenda");
        return imagenPrendaRepository.findAll();
    }

    /**
     * Obtiene una imagen de prenda por su ID.
     * @param imagenprendaId2 
     */
    @Transactional
    public ImagenPrendaEntity getImagenPrenda(Long imagenPrendaId, Long imagenprendaId2) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la imagen de prenda con id = {}", imagenPrendaId);
        Optional<ImagenPrendaEntity> imagenPrendaEntity = imagenPrendaRepository.findById(imagenPrendaId);
        if (imagenPrendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);
        log.info("Termina proceso de consultar la imagen de prenda con id = {}", imagenPrendaId);
        return imagenPrendaEntity.get();
    }

    /**
     * Actualiza una imagen de prenda existente.
     * @param imagenprendaId2 
     */
    @Transactional
    public ImagenPrendaEntity updateImagenPrenda(Long imagenPrendaId, Long imagenprendaId2, ImagenPrendaEntity imagenPrenda)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la imagen de prenda con id = {}", imagenPrendaId);
        Optional<ImagenPrendaEntity> imagenPrendaEntity = imagenPrendaRepository.findById(imagenPrendaId);
        if (imagenPrendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);

        if (imagenPrenda.getImagen() == null || imagenPrenda.getImagen().isBlank())
            throw new IllegalOperationException("El campo 'imagen' no puede ser nulo o vacío");

        imagenPrenda.setId(imagenPrendaId);
        log.info("Termina proceso de actualizar la imagen de prenda con id = {}", imagenPrendaId);
        return imagenPrendaRepository.save(imagenPrenda);
    }

    /**
     * Elimina una imagen de prenda si no está asociada a una prenda o marca.
     * @param imagenprendaId2 
     */
    @Transactional
    public void deleteImagenPrenda(Long imagenPrendaId, Long imagenprendaId2) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la imagen de prenda con id = {}", imagenPrendaId);
        Optional<ImagenPrendaEntity> imagenPrendaEntity = imagenPrendaRepository.findById(imagenPrendaId);
        if (imagenPrendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);

        // Verificar si la imagen está asociada a una prenda o marca
        if (imagenPrendaEntity.get().getPrenda() != null)
            throw new IllegalOperationException("La imagen está asociada a una prenda y no se puede eliminar");

        if (imagenPrendaEntity.get().getMarca() != null)
            throw new IllegalOperationException("La imagen está asociada a una marca y no se puede eliminar");

        imagenPrendaRepository.deleteById(imagenPrendaId);
        log.info("Termina proceso de borrar la imagen de prenda con id = {}", imagenPrendaId);
    }
}