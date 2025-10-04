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
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.CategoriaRepository;
import co.edu.udistrital.mdp.back.repositories.PrendaRepository;
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

    @Transactional
public OutfitEntity createOutfit(OutfitEntity outfitEntity) throws EntityNotFoundException, IllegalOperationException {
    log.info("Inicia proceso de creación del outfit");

    // Validar categorías
    if (outfitEntity.getCategorias() == null || outfitEntity.getCategorias().isEmpty()) {
        throw new IllegalOperationException("Categorias are not valid");
    }

    for (CategoriaEntity categoria : outfitEntity.getCategorias()) {
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoria.getId());
        if (categoriaEntity.isEmpty()) {
            throw new IllegalOperationException("Categoria with id " + categoria.getId() + " is not valid");
        }
    }

    // Validar prendas
    if (outfitEntity.getPrendas() == null || outfitEntity.getPrendas().isEmpty()) {
        throw new IllegalOperationException("Prendas are not valid");
    }

    for (PrendaEntity prenda : outfitEntity.getPrendas()) {
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prenda.getId());
        if (prendaEntity.isEmpty()) {
            throw new IllegalOperationException("Prenda with id " + prenda.getId() + " is not valid");
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
        log.info("Inicia proceso de consultar el outfit con id = {0}", outfitId);
        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
        log.info("Termina proceso de consultar el outfit con id = {0}", outfitId);
        return outfitEntity.get();
    }


    @Transactional
    public OutfitEntity updateOutfit(Long outfitId, OutfitEntity outfit)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar el outfit con id = {0}", outfitId);
        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
        outfit.setId(outfitId);
        log.info("Termina proceso de actualizar el outfit con id = {0}", outfitId);
        return outfitRepository.save(outfit);
    }


    @Transactional
    public void deleteOutfit(Long outfitId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar el outfit con id = {0}", outfitId);
        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        outfitRepository.deleteById(outfitId);
        log.info("Termina proceso de borrar el outfit con id = {0}", outfitId);
    }
}