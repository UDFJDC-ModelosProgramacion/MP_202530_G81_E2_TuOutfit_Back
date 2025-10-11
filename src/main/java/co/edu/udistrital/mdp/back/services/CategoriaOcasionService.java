package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.repositories.CategoriaRepository;
import co.edu.udistrital.mdp.back.repositories.OcasionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CategoriaOcasionService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private OcasionRepository ocasionRepository;

    @Transactional
    public List<OcasionEntity> getOcasionesPorCategoria(Long categoriaId) throws EntityNotFoundException {
        CategoriaEntity categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));
        return categoria.getOcasiones();
    }

    @Transactional
    public OcasionEntity addOcasionACategoria(Long categoriaId, Long ocasionId) throws EntityNotFoundException {
        CategoriaEntity categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));
        OcasionEntity ocasion = ocasionRepository.findById(ocasionId)
                .orElseThrow(() -> new EntityNotFoundException("Ocasion no encontrada"));

        categoria.getOcasiones().add(ocasion);
        categoriaRepository.save(categoria);
        return ocasion;
    }

    @Transactional
    public void removeOcasionDeCategoria(Long categoriaId, Long ocasionId) throws EntityNotFoundException {
        CategoriaEntity categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));
        OcasionEntity ocasion = ocasionRepository.findById(ocasionId)
                .orElseThrow(() -> new EntityNotFoundException("Ocasion no encontrada"));

        categoria.getOcasiones().remove(ocasion);
        categoriaRepository.save(categoria);
    }
}
