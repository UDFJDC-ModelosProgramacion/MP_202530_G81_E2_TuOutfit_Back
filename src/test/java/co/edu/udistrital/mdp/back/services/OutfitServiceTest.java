package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(OutfitService.class)
class OutfitServiceTest {

    @Autowired
    private OutfitService outfitService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<OutfitEntity> outfitList = new ArrayList<>();
    private List<PrendaEntity> prendaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 3; i++) {
            PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
            entityManager.persist(prenda);
            prendaList.add(prenda);
        }

        for (int i = 0; i < 3; i++) {
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            outfit.setPrendas(new ArrayList<>(prendaList)); 
            entityManager.persist(outfit);
            outfitList.add(outfit);
        }
    }

    @Test
    void testCreateOutfit() throws Exception {
        CategoriaEntity categoria = factory.manufacturePojo(CategoriaEntity.class);
        entityManager.persist(categoria);

        PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(prenda);

        OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
        outfit.setCategoria(categoria); 
        outfit.setPrendas(List.of(prenda));

        OutfitEntity nuevo = outfitService.createOutfit(outfit);

        assertNotNull(nuevo);
        assertEquals(outfit.getNombre(), nuevo.getNombre());
        assertEquals(categoria.getId(), nuevo.getCategoria().getId());
    }

    @Test
    void testCreateOutfitWithEmptyPrendas() {
        CategoriaEntity categoria = factory.manufacturePojo(CategoriaEntity.class);
        entityManager.persist(categoria);

        OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
        outfit.setCategoria(categoria);
        outfit.setPrendas(new ArrayList<>());

        assertThrows(IllegalOperationException.class, () -> outfitService.createOutfit(outfit));
    }

    @Test
    void testCreateOutfitWithNullCategoria() {
        OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
        outfit.setCategoria(null); 
        outfit.setPrendas(new ArrayList<>(prendaList));

        assertThrows(IllegalOperationException.class, () -> outfitService.createOutfit(outfit));
    }

    @Test
    void testGetOutfits() {
        List<OutfitEntity> lista = outfitService.getOutfits();
        assertFalse(lista.isEmpty());
        assertEquals(outfitList.size(), lista.size());
    }

    @Test
    void testGetOutfit() throws EntityNotFoundException {
        OutfitEntity entity = outfitList.get(0);
        OutfitEntity encontrado = outfitService.getOutfit(entity.getId());

        assertNotNull(encontrado);
        assertEquals(entity.getId(), encontrado.getId());
    }

    @Test
    void testGetInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> outfitService.getOutfit(999L));
    }

    @Test
    void testUpdateOutfit() throws EntityNotFoundException, IllegalOperationException {
        OutfitEntity existente = outfitList.get(0);
        CategoriaEntity categoria = factory.manufacturePojo(CategoriaEntity.class);
        entityManager.persist(categoria);

        OutfitEntity cambios = factory.manufacturePojo(OutfitEntity.class);
        cambios.setPrendas(new ArrayList<>(prendaList));
        cambios.setCategoria(categoria);
        cambios.setId(existente.getId());

        OutfitEntity actualizado = outfitService.updateOutfit(existente.getId(), cambios);

        assertEquals(cambios.getNombre(), actualizado.getNombre());
        assertEquals(categoria.getId(), actualizado.getCategoria().getId());
    }

    @Test
    void testDeleteOutfit() throws EntityNotFoundException, IllegalOperationException {
        OutfitEntity entity = outfitList.get(0);
        entity.setPrendas(new ArrayList<>());

        outfitService.deleteOutfit(entity.getId());

        OutfitEntity eliminado = entityManager.find(OutfitEntity.class, entity.getId());
        assertNull(eliminado);
    }

    @Test
    void testDeleteInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> outfitService.deleteOutfit(999L));
    }
}
