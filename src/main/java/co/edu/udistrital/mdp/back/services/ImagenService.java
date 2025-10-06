package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.entities.ImagenEntity;
import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.entities.MarcaEntity;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.ImagenRepository;
import co.edu.udistrital.mdp.back.repositories.PrendaRepository;
import co.edu.udistrital.mdp.back.repositories.MarcaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImagenService {


    @Autowired
    OutfitRepository outfitRepository;
     @Autowired
    ImagenRepository imagenRepository;
    @Autowired
    PrendaRepository prendaRepository;
    @Autowired
    MarcaRepository marcaRepository;


    @Transactional
    public ImagenEntity createImagen(ImagenEntity imagenEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de la imagen");

        if (imagenEntity.getOutfit() == null)
            throw new IllegalOperationException("Outfit is not valid");
        
        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(imagenEntity.getOutfit().getId());
        if (outfitEntity.isEmpty())
            throw new IllegalOperationException("Outfit is not valid");

        if (imagenEntity.getPrenda() == null)
            throw new IllegalOperationException("Prenda is not valid");
        
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(imagenEntity.getPrenda().getId());

        if (prendaEntity.isEmpty())
            throw new IllegalOperationException("Prenda is not valid");

        if (imagenEntity.getMarca() == null)
            throw new IllegalOperationException("Marca is not valid");
        
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(imagenEntity.getMarca().getId());
        if (marcaEntity.isEmpty())
            throw new IllegalOperationException("Marca is not valid");

        imagenEntity.setOutfit(outfitEntity.get());
        imagenEntity.setPrenda(prendaEntity.get());
        imagenEntity.setMarca(marcaEntity.get());
        log.info("Termina proceso de creación de la imagen");
        return imagenRepository.save(imagenEntity);
    }



    @Transactional
    public List<ImagenEntity> getImagenes() {
        log.info("Inicia proceso de consultar todoas las imagenes");
        return imagenRepository.findAll();
    }



    @Transactional
    public ImagenEntity getImagen(Long imagenId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la imagen con id = {0}", imagenId);
        Optional<ImagenEntity> imagenEntity = imagenRepository.findById(imagenId);
        if (imagenEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);
        log.info("Termina proceso de consultar la imagen con id = {0}", imagenId);
        return imagenEntity.get();
    }


    @Transactional
    public ImagenEntity updateImagen(Long imagenId, ImagenEntity imagen)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la imagen con id = {0}", imagenId);
        Optional<ImagenEntity> imagenEntity = imagenRepository.findById(imagenId);
        if (imagenEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);
        imagen.setId(imagenId);
        log.info("Termina proceso de actualizar la imagen con id = {0}", imagenId);
        return imagenRepository.save(imagen);
    }


    @Transactional
    public void deleteImagen(Long imagenId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la imagen con id = {0}", imagenId);
        Optional<ImagenEntity> imagenEntity = imagenRepository.findById(imagenId);
        if (imagenEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.IMAGEN_NOT_FOUND);

        if (imagenEntity.get().getOutfit() != null)
            throw new IllegalOperationException("Imagen asociada a un outfit");
        if (imagenEntity.get().getPrenda() != null)
            throw new IllegalOperationException("Imagen asociada a una prenda");
        if (imagenEntity.get().getMarca() != null)
            throw new IllegalOperationException("Imagen asociada a una marca");

        imagenRepository.deleteById(imagenId);
        log.info("Termina proceso de borrar la imagen con id = {0}", imagenId);
    }
}