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

import co.edu.udistrital.mdp.back.entities.ListaDeseosEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ListaDeseosOutfitService.class)
class ListaDeseosOutfitServiceTest {

    @Autowired
    private ListaDeseosOutfitService listaDeseosOutfitService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private ListaDeseosEntity listaDeseos;
    private List<OutfitEntity> outfitList = new ArrayList<>();

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ListaDeseosEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        listaDeseos = factory.manufacturePojo(ListaDeseosEntity.class);
        entityManager.persist(listaDeseos);

        for (int i = 0; i < 3; i++) {
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfit);
            outfitList.add(outfit);
            listaDeseos.getOutfits().add(outfit);
        }
        entityManager.persist(listaDeseos);
    }

    /**
     * Prueba para agregar un outfit a una lista de deseos.
     */
    @Test
    void testAddOutfitCorrecto() throws EntityNotFoundException, IllegalOperationException {
        OutfitEntity nuevo = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(nuevo);

        OutfitEntity result = listaDeseosOutfitService.addOutfit(listaDeseos.getId(), nuevo.getId());
        assertNotNull(result);
        assertTrue(listaDeseosOutfitService.getOutfits(listaDeseos.getId()).contains(nuevo));
    }

    /**
     * Prueba para agregar un outfit a una lista inexistente.
     */
    @Test
    void testAddOutfitListaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfit);
            listaDeseosOutfitService.addOutfit(0L, outfit.getId());
        });
    }

    /**
     * Prueba para agregar un outfit inexistente.
     */
    @Test
    void testAddOutfitInexistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            listaDeseosOutfitService.addOutfit(listaDeseos.getId(), 0L);
        });
    }

    /**
     * Prueba para consultar los outfits de una lista.
     */
    @Test
    void testGetOutfits() throws EntityNotFoundException {
        List<OutfitEntity> outfits = listaDeseosOutfitService.getOutfits(listaDeseos.getId());
        assertEquals(outfitList.size(), outfits.size());
    }

    /**
     * Prueba para consultar los outfits de una lista inexistente.
     */
    @Test
    void testGetOutfitsListaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> listaDeseosOutfitService.getOutfits(0L));
    }

    /**
     * Prueba para consultar un outfit asociado correctamente.
     */
    @Test
    void testGetOutfit() throws EntityNotFoundException, IllegalOperationException {
        OutfitEntity outfit = outfitList.get(0);
        OutfitEntity result = listaDeseosOutfitService.getOutfit(listaDeseos.getId(), outfit.getId());
        assertNotNull(result);
        assertEquals(outfit.getId(), result.getId());
    }

    /**
     * Prueba para consultar un outfit de una lista inexistente.
     */
    @Test
    void testGetOutfitListaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            OutfitEntity outfit = outfitList.get(0);
            listaDeseosOutfitService.getOutfit(0L, outfit.getId());
        });
    }

    /**
     * Prueba para consultar un outfit inexistente en una lista válida.
     */
    @Test
    void testGetOutfitInexistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            listaDeseosOutfitService.getOutfit(listaDeseos.getId(), 0L);
        });
    }

    /**
     * Prueba para consultar un outfit no asociado a la lista.
     */
    @Test
    void testGetOutfitNoAsociado() {
        assertThrows(IllegalOperationException.class, () -> {
            OutfitEntity nuevo = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(nuevo);
            listaDeseosOutfitService.getOutfit(listaDeseos.getId(), nuevo.getId());
        });
    }

    /**
     * Prueba para reemplazar los outfits de una lista.
     */
    @Test
    void testReplaceOutfits() throws EntityNotFoundException {
        List<OutfitEntity> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfit);
            nuevaLista.add(outfit);
        }

        listaDeseosOutfitService.replaceOutfits(listaDeseos.getId(), nuevaLista);

        List<OutfitEntity> outfits = listaDeseosOutfitService.getOutfits(listaDeseos.getId());
        for (OutfitEntity o : nuevaLista) {
            assertTrue(outfits.contains(o));
        }
    }

    /**
     * Prueba para reemplazar outfits en una lista inexistente.
     */
    @Test
    void testReplaceOutfitsListaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<OutfitEntity> nuevaLista = new ArrayList<>();
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfit);
            nuevaLista.add(outfit);
            listaDeseosOutfitService.replaceOutfits(0L, nuevaLista);
        });
    }

    /**
     * Prueba para eliminar un outfit de la lista.
     */
    @Test
    void testRemoveOutfit() throws EntityNotFoundException {
        OutfitEntity outfit = outfitList.get(0);
        listaDeseosOutfitService.removeOutfit(listaDeseos.getId(), outfit.getId());
        assertFalse(listaDeseosOutfitService.getOutfits(listaDeseos.getId()).contains(outfit));
    }

    /**
     * Prueba para eliminar un outfit inexistente.
     */
    @Test
    void testRemoveOutfitInexistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            listaDeseosOutfitService.removeOutfit(listaDeseos.getId(), 0L);
        });
    }

    /**
     * Prueba para eliminar un outfit de una lista inexistente.
     */
    @Test
    void testRemoveOutfitListaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            OutfitEntity outfit = outfitList.get(0);
            listaDeseosOutfitService.removeOutfit(0L, outfit.getId());
        });
    }
}
