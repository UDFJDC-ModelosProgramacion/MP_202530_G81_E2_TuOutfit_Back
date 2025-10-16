package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import co.edu.udistrital.mdp.back.repositories.CategoriaRepository;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoriaService {

    @Autowired
    OutfitRepository outfitRepository;

    @Autowired
    CategoriaRepository categoriaRepository;


    @Transactional
    public CategoriaEntity createCategoria(CategoriaEntity categoriaEntity)
            throws IllegalOperationException {

        log.info("Inicia proceso de creación de la categoría");

        if (categoriaEntity.getNombre() == null || categoriaEntity.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre de la categoría no puede estar vacío");
        }


        List<CategoriaEntity> existentes = categoriaRepository.findAll();
        boolean existe = existentes.stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(categoriaEntity.getNombre()));
        if (existe) {
            throw new IllegalOperationException("Ya existe una categoría con el nombre: " + categoriaEntity.getNombre());
        }

        log.info("Termina proceso de creación de la categoría");
        return categoriaRepository.save(categoriaEntity);
    }


    @Transactional
    public List<CategoriaEntity> getCategorias() {
        log.info("Inicia proceso de consultar todas las categorías");
        return categoriaRepository.findAll();
    }

    @Transactional
    public CategoriaEntity getCategoria(Long categoriaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la categoría con id = {}", categoriaId);
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        if (categoriaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);
        log.info("Termina proceso de consultar la categoría con id = {}", categoriaId);
        return categoriaEntity.get();
    }


    @Transactional
    public CategoriaEntity updateCategoria(Long categoriaId, CategoriaEntity categoria)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la categoría con id = {}", categoriaId);
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        if (categoriaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre de la categoría no puede estar vacío");
        }

        categoria.setId(categoriaId);
        log.info("Termina proceso de actualizar la categoría con id = {}", categoriaId);
        return categoriaRepository.save(categoria);
    }


    @Transactional
    public void deleteCategoria(Long categoriaId)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de borrar la categoría con id = {}", categoriaId);
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        if (categoriaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

        CategoriaEntity categoria = categoriaEntity.get();


        if (categoria.getOutfits() != null && !categoria.getOutfits().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar la categoría porque tiene outfits asociados");
        }

        categoriaRepository.deleteById(categoriaId);
        log.info("Termina proceso de borrar la categoría con id = {}", categoriaId);
    }


    @Transactional
    public List<OutfitEntity> getOutfitsByCategoria(Long categoriaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar los outfits de la categoría con id = {}", categoriaId);
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        if (categoriaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

        return categoriaEntity.get().getOutfits();
    }
}