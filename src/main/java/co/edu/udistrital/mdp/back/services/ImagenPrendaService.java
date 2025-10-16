package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.ImagenPrendaEntity;
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

    /**
     * Crea una nueva ImagenPrenda.
     */
    @Transactional
    public ImagenPrendaEntity createImagenPrenda(ImagenPrendaEntity imagenPrendaEntity)
            throws IllegalOperationException {
        log.info("Inicia proceso de creación de una imagen de prenda");

        if (imagenPrendaEntity.getImagen() == null || imagenPrendaEntity.getImagen().isBlank())
            throw new IllegalOperationException("El campo 'imagen' no puede ser nulo o vacío");

        log.info("Termina proceso de creación de una imagen de prenda");
        return imagenPrendaRepository.save(imagenPrendaEntity);
    }

    /**
     * Obtiene todas las imágenes de prenda.
     */
    @Transactional
    public List<ImagenPrendaEntity> getImagenesPrenda() {
        log.info("Inicia proceso de consultar todas las imágenes de prenda");
        return imagenPrendaRepository.findAll();
    }

    /**
     * Obtiene una imagen de prenda por su ID.
     */
    @Transactional
    public ImagenPrendaEntity getImagenPrenda(Long imagenPrendaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la imagen de prenda con id = {}", imagenPrendaId);
        Optional<ImagenPrendaEntity> imagenPrendaEntity = imagenPrendaRepository.findById(imagenPrendaId);
        if (imagenPrendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);
        log.info("Termina proceso de consultar la imagen de prenda con id = {}", imagenPrendaId);
        return imagenPrendaEntity.get();
    }

    /**
     * Actualiza una imagen de prenda existente.
     */
    @Transactional
    public ImagenPrendaEntity updateImagenPrenda(Long imagenPrendaId, ImagenPrendaEntity imagenPrenda)
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
     */
    @Transactional
    public void deleteImagenPrenda(Long imagenPrendaId) throws EntityNotFoundException, IllegalOperationException {
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