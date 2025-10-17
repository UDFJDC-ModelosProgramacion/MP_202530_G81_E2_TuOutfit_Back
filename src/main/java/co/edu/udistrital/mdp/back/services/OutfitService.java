package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.entities.RecomendacionEntity;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import co.edu.udistrital.mdp.back.repositories.CategoriaRepository;
import co.edu.udistrital.mdp.back.repositories.PrendaRepository;
import co.edu.udistrital.mdp.back.repositories.RecomendacionRepository;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OutfitService {

    @Autowired
    OutfitRepository outfitRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    PrendaRepository prendaRepository;

    @Autowired
    RecomendacionRepository recomendacionRepository;

    
    @Transactional
    public OutfitEntity createOutfit(OutfitEntity outfitEntity)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de creación del outfit");

        
        if (outfitEntity.getCategoria() == null || outfitEntity.getCategoria().getId() == null) {
            throw new IllegalOperationException("La categoría del outfit no es válida");
        }

        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(outfitEntity.getCategoria().getId());
        if (categoriaEntity.isEmpty()) {
            throw new IllegalOperationException("La categoría con id " + outfitEntity.getCategoria().getId() + " no existe");
        }

    
        if (outfitEntity.getPrendas() == null || outfitEntity.getPrendas().isEmpty()) {
            throw new IllegalOperationException("Debe asociar al menos una prenda al outfit");
        }

        for (PrendaEntity prenda : outfitEntity.getPrendas()) {
            if (prenda.getId() == null || prendaRepository.findById(prenda.getId()).isEmpty()) {
                throw new IllegalOperationException("La prenda con id " + prenda.getId() + " no es válida");
            }
        }

     
        outfitEntity.setCategoria(categoriaEntity.get());

      
        if (outfitEntity.getRecomendaciones() != null) {
            for (RecomendacionEntity rec : outfitEntity.getRecomendaciones()) {
                rec.setOutfit(outfitEntity); 
            }
        }

        log.info("Termina proceso de creación del outfit");
        return outfitRepository.save(outfitEntity);
    }

   
    @Transactional
    public List<OutfitEntity> getOutfits() {
        log.info("Inicia proceso de consultar todos los outfits");
        return outfitRepository.findAll();
    }

    
    @Transactional
    public OutfitEntity getOutfit(Long outfitId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el outfit con id = {}", outfitId);
        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
        log.info("Termina proceso de consultar el outfit con id = {}", outfitId);
        return outfitEntity.get();
    }

    
    @Transactional
    public OutfitEntity updateOutfit(Long outfitId, OutfitEntity outfit)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de actualizar el outfit con id = {}", outfitId);
        Optional<OutfitEntity> existingOutfit = outfitRepository.findById(outfitId);
        if (existingOutfit.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

    
        if (outfit.getCategoria() != null && outfit.getCategoria().getId() != null) {
            Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(outfit.getCategoria().getId());
            if (categoriaEntity.isEmpty()) {
                throw new IllegalOperationException("La categoría con id " + outfit.getCategoria().getId() + " no existe");
            }
            outfit.setCategoria(categoriaEntity.get());
        }

      
        if (outfit.getRecomendaciones() != null) {
            for (RecomendacionEntity rec : outfit.getRecomendaciones()) {
                rec.setOutfit(outfit);
            }
        }

        outfit.setId(outfitId);
        log.info("Termina proceso de actualizar el outfit con id = {}", outfitId);
        return outfitRepository.save(outfit);
    }


    @Transactional
    public void deleteOutfit(Long outfitId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar el outfit con id = {}", outfitId);
        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        outfitRepository.deleteById(outfitId);
        log.info("Termina proceso de borrar el outfit con id = {}", outfitId);
    }
}
