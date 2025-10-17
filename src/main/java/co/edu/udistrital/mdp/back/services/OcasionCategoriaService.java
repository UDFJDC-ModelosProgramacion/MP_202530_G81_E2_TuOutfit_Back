package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.repositories.CategoriaRepository;
import co.edu.udistrital.mdp.back.repositories.OcasionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class OcasionCategoriaService {

    @Autowired
    private OcasionRepository ocasionRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public List<CategoriaEntity> getCategoriasPorOcasion(Long ocasionId) throws EntityNotFoundException {
        OcasionEntity ocasion = ocasionRepository.findById(ocasionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OCASION_NOT_FOUND));
        return ocasion.getCategorias();
    }

    @Transactional
    public CategoriaEntity addCategoriaAOcasion(Long ocasionId, Long categoriaId) throws EntityNotFoundException {
        OcasionEntity ocasion = ocasionRepository.findById(ocasionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OCASION_NOT_FOUND));
        CategoriaEntity categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND));

        ocasion.getCategorias().add(categoria);
        ocasionRepository.save(ocasion);
        return categoria;
    }

    @Transactional
    public void removeCategoriaDeOcasion(Long ocasionId, Long categoriaId) throws EntityNotFoundException {
        OcasionEntity ocasion = ocasionRepository.findById(ocasionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OCASION_NOT_FOUND));
        CategoriaEntity categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND));

        ocasion.getCategorias().remove(categoria);
        ocasionRepository.save(ocasion);
    }
}
