package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.entities.CategoriaEntity;

import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import co.edu.udistrital.mdp.back.repositories.PrendaRepository;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.CategoriaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoriaService {


    @Autowired
    OutfitRepository outfitRepository;
     @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    PrendaRepository prendaRepository;

    @Transactional
    public CategoriaEntity createCategoria(CategoriaEntity categoriaEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de la categoria");

        log.info("Termina proceso de creación de la categoria");
        return categoriaRepository.save(categoriaEntity);
    }



    @Transactional
    public List<CategoriaEntity> getCategorias() {
        log.info("Inicia proceso de consultar todas las categorias");
        return categoriaRepository.findAll();
    }



    @Transactional
    public CategoriaEntity getCategoria(Long categoriaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la categoria con id = {0}", categoriaId);
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        if (categoriaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);
        log.info("Termina proceso de consultar la categoria con id = {0}", categoriaId);
        return categoriaEntity.get();
    }


    @Transactional
    public CategoriaEntity updateCategoria(Long categoriaId, CategoriaEntity categoria)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la categoria con id = {0}", categoriaId);
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        if (categoriaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);
        categoria.setId(categoriaId);
        log.info("Termina proceso de actualizar la categoria con id = {0}", categoriaId);
        return categoriaRepository.save(categoria);
    }


    @Transactional
    public void deleteCategoria(Long categoriaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la categoria con id = {0}", categoriaId);
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        if (categoriaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

        List<PrendaEntity> prendas = categoriaEntity.get().getPrendas();

        if (!prendas.isEmpty())
            throw new IllegalOperationException("Unable to delete category because it has associated prendas");

        categoriaRepository.deleteById(categoriaId);
        log.info("Termina proceso de borrar la categoria con id = {0}", categoriaId);

    }
}