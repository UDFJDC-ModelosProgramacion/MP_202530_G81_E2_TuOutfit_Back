package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.repositories.CategoriaRepository;
import co.edu.udistrital.mdp.back.repositories.OcasionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(OcasionCategoriaService.class)
class OcasionCategoriaServiceTest {

    @Autowired
    private OcasionCategoriaService ocasionCategoriaService;

    @Autowired
    private OcasionRepository ocasionRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private OcasionEntity ocasion;
    private CategoriaEntity categoria;

    @BeforeEach
    void setUp() {
        ocasion = new OcasionEntity();
        ocasion.setNombre("Boda");
        ocasionRepository.save(ocasion);

        categoria = new CategoriaEntity();
        categoria.setNombre("Formal");
        categoriaRepository.save(categoria);
    }

    @Test
    void testAddCategoriaAOcasion() throws Exception {
        CategoriaEntity added = ocasionCategoriaService.addCategoriaAOcasion(ocasion.getId(), categoria.getId());
        assertNotNull(added);
        assertEquals("Formal", added.getNombre());

        OcasionEntity updated = ocasionRepository.findById(ocasion.getId()).orElseThrow();
        assertTrue(updated.getCategorias().contains(categoria));
    }

    @Test
    void testGetCategoriasPorOcasion() throws Exception {
        ocasionCategoriaService.addCategoriaAOcasion(ocasion.getId(), categoria.getId());
        List<CategoriaEntity> categorias = ocasionCategoriaService.getCategoriasPorOcasion(ocasion.getId());

        assertFalse(categorias.isEmpty());
        assertEquals("Formal", categorias.get(0).getNombre());
    }

    @Test
    void testRemoveCategoriaDeOcasion() throws Exception {
        ocasionCategoriaService.addCategoriaAOcasion(ocasion.getId(), categoria.getId());
        ocasionCategoriaService.removeCategoriaDeOcasion(ocasion.getId(), categoria.getId());

        OcasionEntity updated = ocasionRepository.findById(ocasion.getId()).orElseThrow();
        assertFalse(updated.getCategorias().contains(categoria));
    }

    @Test
    void testAddCategoriaAOcasionNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> ocasionCategoriaService.addCategoriaAOcasion(999L, categoria.getId()));
    }

    @Test
    void testAddCategoriaNoExistenteAOcasion() {
        assertThrows(EntityNotFoundException.class, () -> ocasionCategoriaService.addCategoriaAOcasion(ocasion.getId(), 999L));
    }
}
