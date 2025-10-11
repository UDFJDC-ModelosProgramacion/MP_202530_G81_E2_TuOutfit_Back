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
@Import(CategoriaOcasionService.class)
class CategoriaOcasionServiceTest {

    @Autowired
    private CategoriaOcasionService categoriaOcasionService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private OcasionRepository ocasionRepository;

    private CategoriaEntity categoria;
    private OcasionEntity ocasion;

    @BeforeEach
    void setUp() {
        categoria = new CategoriaEntity();
        categoria.setNombre("Deportiva");
        categoriaRepository.save(categoria);

        ocasion = new OcasionEntity();
        ocasion.setNombre("Picnic");
        ocasionRepository.save(ocasion);
    }

    @Test
    void testAddOcasionACategoria() throws Exception {
        OcasionEntity added = categoriaOcasionService.addOcasionACategoria(categoria.getId(), ocasion.getId());
        assertNotNull(added);
        assertEquals("Picnic", added.getNombre());

        CategoriaEntity updated = categoriaRepository.findById(categoria.getId()).orElseThrow();
        assertTrue(updated.getOcasiones().contains(ocasion));
    }

    @Test
    void testGetOcasionesPorCategoria() throws Exception {
        categoriaOcasionService.addOcasionACategoria(categoria.getId(), ocasion.getId());
        List<OcasionEntity> ocasiones = categoriaOcasionService.getOcasionesPorCategoria(categoria.getId());

        assertFalse(ocasiones.isEmpty());
        assertEquals("Picnic", ocasiones.get(0).getNombre());
    }

    @Test
    void testRemoveOcasionDeCategoria() throws Exception {
        categoriaOcasionService.addOcasionACategoria(categoria.getId(), ocasion.getId());
        categoriaOcasionService.removeOcasionDeCategoria(categoria.getId(), ocasion.getId());

        CategoriaEntity updated = categoriaRepository.findById(categoria.getId()).orElseThrow();
        assertFalse(updated.getOcasiones().contains(ocasion));
    }

    @Test
    void testAddOcasionACategoriaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> categoriaOcasionService.addOcasionACategoria(999L, ocasion.getId()));
    }

    @Test
    void testAddOcasionNoExistenteACategoria() {
        assertThrows(EntityNotFoundException.class, () -> categoriaOcasionService.addOcasionACategoria(categoria.getId(), 999L));
    }
}
