package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.repositories.CategoriaRepository;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(CategoriaService.class)
class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private OutfitRepository outfitRepository;

    private CategoriaEntity categoria;

    @BeforeEach
    void setUp() {
        categoria = new CategoriaEntity();
        categoria.setNombre("Deportiva");
        categoria.setEdad(25);
        categoria.setOutfits(new ArrayList<>());
        categoriaRepository.save(categoria);
    }

    @Test
    void testCreateCategoria() throws Exception {
        CategoriaEntity nueva = new CategoriaEntity();
        nueva.setNombre("Casual");
        nueva.setEdad(30);

        CategoriaEntity creada = categoriaService.createCategoria(nueva);

        assertNotNull(creada);
        assertEquals("Casual", creada.getNombre());
        assertTrue(categoriaRepository.findById(creada.getId()).isPresent());
    }

    @Test
    void testGetCategorias() {
        List<CategoriaEntity> categorias = categoriaService.getCategorias();

        assertFalse(categorias.isEmpty());
        assertEquals("Deportiva", categorias.get(0).getNombre());
    }

    @Test
    void testGetCategoriaPorId() throws Exception {
        CategoriaEntity encontrada = categoriaService.getCategoria(categoria.getId());

        assertNotNull(encontrada);
        assertEquals("Deportiva", encontrada.getNombre());
    }

    @Test
    void testGetCategoriaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> categoriaService.getCategoria(999L));
    }

    @Test
    void testUpdateCategoria() throws Exception {
        CategoriaEntity actualizada = new CategoriaEntity();
        actualizada.setNombre("Formal");
        actualizada.setEdad(40);

        CategoriaEntity result = categoriaService.updateCategoria(categoria.getId(), actualizada);

        assertEquals("Formal", result.getNombre());
        assertEquals(categoria.getId(), result.getId());
    }

    @Test
    void testUpdateCategoriaNoExistente() {
        CategoriaEntity actualizada = new CategoriaEntity();
        actualizada.setNombre("Casual");
        actualizada.setEdad(30);

        assertThrows(EntityNotFoundException.class, () -> categoriaService.updateCategoria(999L, actualizada));
    }

    @Test
    void testDeleteCategoriaSinOutfits() throws Exception {
        CategoriaEntity nueva = new CategoriaEntity();
        nueva.setNombre("Accesorios");
        nueva.setEdad(18);
        nueva.setOutfits(new ArrayList<>());
        categoriaRepository.save(nueva);

        categoriaService.deleteCategoria(nueva.getId());

        assertFalse(categoriaRepository.findById(nueva.getId()).isPresent());
    }

    @Test
    void testDeleteCategoriaConOutfitsLanzaExcepcion() {
        OutfitEntity outfit = new OutfitEntity();
        outfit.setNombre("Outfit Deportivo");
        outfit.setCategoria(categoria);
        outfitRepository.save(outfit);

        categoria.getOutfits().add(outfit);
        categoriaRepository.save(categoria);

        assertThrows(Exception.class, () -> categoriaService.deleteCategoria(categoria.getId()));
    }

    @Test
    void testDeleteCategoriaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> categoriaService.deleteCategoria(888L));
    }
}