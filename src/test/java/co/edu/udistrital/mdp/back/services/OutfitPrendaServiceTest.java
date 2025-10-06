package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(OutfitPrendaService.class)
class OutfitPrendaServiceTest {

    @Autowired
    private OutfitPrendaService outfitPrendaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private OutfitEntity outfit = new OutfitEntity();
    private List<PrendaEntity> prendaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
    }

    private void insertData() {
        outfit = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(outfit);

        for (int i = 0; i < 3; i++) {
            PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
            entityManager.persist(entity);
            entity.getOutfits().add(outfit);
            outfit.getPrendas().add(entity);
            prendaList.add(entity);
        }
    }

    @Test
    void testAddPrenda() throws EntityNotFoundException, IllegalOperationException {
        OutfitEntity newOutfit = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(newOutfit);

        PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(prenda);

        outfitPrendaService.addPrenda(newOutfit.getId(), prenda.getId());

        PrendaEntity stored = outfitPrendaService.getPrenda(newOutfit.getId(), prenda.getId());
        assertEquals(prenda.getId(), stored.getId());
    }

    @Test
    void testAddPrendaInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
            entityManager.persist(prenda);
            outfitPrendaService.addPrenda(0L, prenda.getId());
        });
    }

    @Test
    void testAddInvalidPrenda() {
        assertThrows(EntityNotFoundException.class, () -> {
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfit);
            outfitPrendaService.addPrenda(outfit.getId(), 0L);
        });
    }

    @Test
    void testGetPrendas() throws EntityNotFoundException {
        List<PrendaEntity> prendas = outfitPrendaService.getPrendas(outfit.getId());
        assertEquals(prendaList.size(), prendas.size());
        for (PrendaEntity p : prendaList) {
            assertTrue(prendas.contains(p));
        }
    }

    @Test
    void testGetPrendasInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            outfitPrendaService.getPrendas(0L);
        });
    }

    @Test
    void testGetPrenda() throws EntityNotFoundException, IllegalOperationException {
        PrendaEntity prenda = prendaList.get(0);
        PrendaEntity found = outfitPrendaService.getPrenda(outfit.getId(), prenda.getId());
        assertNotNull(found);
        assertEquals(prenda.getId(), found.getId());
    }

    @Test
    void testGetPrendaInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            outfitPrendaService.getPrenda(0L, prendaList.get(0).getId());
        });
    }

    @Test
    void testGetPrendaInvalidPrenda() {
        assertThrows(EntityNotFoundException.class, () -> {
            outfitPrendaService.getPrenda(outfit.getId(), 0L);
        });
    }

    @Test
    void testGetNotAssociatedPrenda() {
        assertThrows(IllegalOperationException.class, () -> {
            OutfitEntity newOutfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(newOutfit);
            PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
            entityManager.persist(prenda);
            outfitPrendaService.getPrenda(newOutfit.getId(), prenda.getId());
        });
    }

    @Test
    void testReplacePrendas() throws EntityNotFoundException {
        List<PrendaEntity> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
            entityManager.persist(entity);
            nuevaLista.add(entity);
        }

        outfitPrendaService.replacePrendas(outfit.getId(), nuevaLista);
        List<PrendaEntity> stored = outfitPrendaService.getPrendas(outfit.getId());
        for (PrendaEntity prenda : nuevaLista) {
            assertTrue(stored.contains(prenda));
        }
    }

    @Test
    void testReplacePrendasInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<PrendaEntity> nuevaLista = new ArrayList<>();
            PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
            entity.setId(0L);
            nuevaLista.add(entity);
            outfitPrendaService.replacePrendas(0L, nuevaLista);
        });
    }

    @Test
    void testRemovePrenda() throws EntityNotFoundException {
        PrendaEntity prenda = prendaList.get(0);
        outfitPrendaService.removePrenda(outfit.getId(), prenda.getId());
        assertFalse(outfitPrendaService.getPrendas(outfit.getId()).contains(prenda));
    }

    @Test
    void testRemovePrendaInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            outfitPrendaService.removePrenda(0L, prendaList.get(0).getId());
        });
    }

    @Test
    void testRemovePrendaInvalidPrenda() {
        assertThrows(EntityNotFoundException.class, () -> {
            outfitPrendaService.removePrenda(outfit.getId(), 0L);
        });
    }
}
