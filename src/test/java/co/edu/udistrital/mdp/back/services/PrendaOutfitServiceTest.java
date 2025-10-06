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
@Import(PrendaOutfitService.class)
class PrendaOutfitServiceTest {

    @Autowired
    private PrendaOutfitService prendaOutfitService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private PrendaEntity prenda = new PrendaEntity();
    private List<OutfitEntity> outfitList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
    }

    private void insertData() {
        prenda = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(prenda);

        for (int i = 0; i < 3; i++) {
            OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(entity);
            entity.getPrendas().add(prenda);
            prenda.getOutfits().add(entity);
            outfitList.add(entity);
        }
    }

    @Test
    void testAddOutfit() throws EntityNotFoundException, IllegalOperationException {
        PrendaEntity newPrenda = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(newPrenda);

        OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(outfit);

        prendaOutfitService.addOutfit(newPrenda.getId(), outfit.getId());

        OutfitEntity stored = prendaOutfitService.getOutfit(newPrenda.getId(), outfit.getId());
        assertEquals(outfit.getId(), stored.getId());
    }

    @Test
    void testAddOutfitInvalidPrenda() {
        assertThrows(EntityNotFoundException.class, () -> {
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfit);
            prendaOutfitService.addOutfit(0L, outfit.getId());
        });
    }

    @Test
    void testAddInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            PrendaEntity newPrenda = factory.manufacturePojo(PrendaEntity.class);
            entityManager.persist(newPrenda);
            prendaOutfitService.addOutfit(newPrenda.getId(), 0L);
        });
    }

    @Test
    void testGetOutfits() throws EntityNotFoundException {
        List<OutfitEntity> outfits = prendaOutfitService.getOutfits(prenda.getId());
        assertEquals(outfitList.size(), outfits.size());
        for (OutfitEntity o : outfitList) {
            assertTrue(outfits.contains(o));
        }
    }

    @Test
    void testGetOutfitsInvalidPrenda() {
        assertThrows(EntityNotFoundException.class, () -> {
            prendaOutfitService.getOutfits(0L);
        });
    }

    @Test
    void testGetOutfit() throws EntityNotFoundException, IllegalOperationException {
        OutfitEntity outfit = outfitList.get(0);
        OutfitEntity found = prendaOutfitService.getOutfit(prenda.getId(), outfit.getId());
        assertNotNull(found);
        assertEquals(outfit.getId(), found.getId());
    }

    @Test
    void testGetOutfitInvalidPrenda() {
        assertThrows(EntityNotFoundException.class, () -> {
            prendaOutfitService.getOutfit(0L, outfitList.get(0).getId());
        });
    }

    @Test
    void testGetOutfitInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            prendaOutfitService.getOutfit(prenda.getId(), 0L);
        });
    }

    @Test
    void testGetNotAssociatedOutfit() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newPrenda = factory.manufacturePojo(PrendaEntity.class);
            entityManager.persist(newPrenda);
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfit);
            prendaOutfitService.getOutfit(newPrenda.getId(), outfit.getId());
        });
    }

    @Test
    void testAddOutfits() throws EntityNotFoundException {
        List<OutfitEntity> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(entity);
            nuevaLista.add(entity);
        }

        prendaOutfitService.addOutfits(prenda.getId(), nuevaLista);
        List<OutfitEntity> stored = prendaOutfitService.getOutfits(prenda.getId());
        for (OutfitEntity outfit : nuevaLista) {
            assertTrue(stored.contains(outfit));
        }
    }

    @Test
    void testAddOutfitsInvalidPrenda() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<OutfitEntity> nuevaLista = new ArrayList<>();
            OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
            entity.setId(0L);
            nuevaLista.add(entity);
            prendaOutfitService.addOutfits(0L, nuevaLista);
        });
    }

    @Test
    void testRemoveOutfit() throws EntityNotFoundException {
        OutfitEntity outfit = outfitList.get(0);
        prendaOutfitService.removeOutfit(prenda.getId(), outfit.getId());
        assertFalse(prendaOutfitService.getOutfits(prenda.getId()).contains(outfit));
    }

    @Test
    void testRemoveOutfitInvalidPrenda() {
        assertThrows(EntityNotFoundException.class, () -> {
            prendaOutfitService.removeOutfit(0L, outfitList.get(0).getId());
        });
    }

    @Test
    void testRemoveOutfitInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            prendaOutfitService.removeOutfit(prenda.getId(), 0L);
        });
    }
}

