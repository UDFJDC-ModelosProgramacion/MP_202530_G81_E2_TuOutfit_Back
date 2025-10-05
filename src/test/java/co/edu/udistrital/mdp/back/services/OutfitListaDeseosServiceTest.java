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
@Import(OutfitListaDeseosService.class)
class OutfitListaDeseosServiceTest {

    @Autowired
    private OutfitListaDeseosService outfitListaDeseosService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private OutfitEntity outfit;
    private List<ListaDeseosEntity> listaDeseosList = new ArrayList<>();

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
        entityManager.getEntityManager().createQuery("delete from ListaDeseosEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        outfit = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(outfit);

        for (int i = 0; i < 3; i++) {
            ListaDeseosEntity lista = factory.manufacturePojo(ListaDeseosEntity.class);
            entityManager.persist(lista);
            listaDeseosList.add(lista);
            outfit.getListasDeseos().add(lista);
        }
        entityManager.persist(outfit);
    }

    /**
     * Prueba para asociar una lista de deseos a un outfit.
     */
    @Test
    void testAddListaDeseosCorrecto() throws EntityNotFoundException, IllegalOperationException {
        ListaDeseosEntity nuevaLista = factory.manufacturePojo(ListaDeseosEntity.class);
        entityManager.persist(nuevaLista);

        ListaDeseosEntity result = outfitListaDeseosService.addListaDeseos(outfit.getId(), nuevaLista.getId());
        assertNotNull(result);
        assertTrue(outfitListaDeseosService.getListasDeseos(outfit.getId()).contains(nuevaLista));
    }

    /**
     * Prueba para asociar una lista de deseos a un outfit inexistente.
     */
    @Test
    void testAddListaDeseosOutfitNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            ListaDeseosEntity lista = factory.manufacturePojo(ListaDeseosEntity.class);
            entityManager.persist(lista);
            outfitListaDeseosService.addListaDeseos(0L, lista.getId());
        });
    }

    /**
     * Prueba para asociar una lista de deseos inexistente a un outfit.
     */
    @Test
    void testAddListaDeseosInexistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            outfitListaDeseosService.addListaDeseos(outfit.getId(), 0L);
        });
    }

    /**
     * Prueba para consultar las listas de deseos de un outfit.
     */
    @Test
    void testGetListasDeseos() throws EntityNotFoundException {
        List<ListaDeseosEntity> listas = outfitListaDeseosService.getListasDeseos(outfit.getId());
        assertEquals(listaDeseosList.size(), listas.size());
    }

    /**
     * Prueba para consultar las listas de un outfit inexistente.
     */
    @Test
    void testGetListasDeseosOutfitNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> outfitListaDeseosService.getListasDeseos(0L));
    }

    /**
     * Prueba para obtener una lista de deseos asociada correctamente.
     */
    @Test
    void testGetListaDeseosCorrecto() throws EntityNotFoundException, IllegalOperationException {
        ListaDeseosEntity lista = listaDeseosList.get(0);
        ListaDeseosEntity result = outfitListaDeseosService.getListaDeseos(outfit.getId(), lista.getId());
        assertNotNull(result);
        assertEquals(lista.getId(), result.getId());
    }

    /**
     * Prueba para obtener una lista inexistente.
     */
    @Test
    void testGetListaDeseosInexistente() {
        assertThrows(EntityNotFoundException.class, () -> outfitListaDeseosService.getListaDeseos(outfit.getId(), 0L));
    }

    /**
     * Prueba para obtener una lista de un outfit inexistente.
     */
    @Test
    void testGetListaDeseosOutfitNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            ListaDeseosEntity lista = listaDeseosList.get(0);
            outfitListaDeseosService.getListaDeseos(0L, lista.getId());
        });
    }

    /**
     * Prueba para obtener una lista no asociada a un outfit.
     */
    @Test
    void testGetListaDeseosNoAsociada() {
        assertThrows(IllegalOperationException.class, () -> {
            ListaDeseosEntity nueva = factory.manufacturePojo(ListaDeseosEntity.class);
            entityManager.persist(nueva);
            outfitListaDeseosService.getListaDeseos(outfit.getId(), nueva.getId());
        });
    }

    /**
     * Prueba para reemplazar las listas de deseos asociadas a un outfit.
     */
    @Test
    void testReplaceListasDeseos() throws EntityNotFoundException {
        List<ListaDeseosEntity> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ListaDeseosEntity lista = factory.manufacturePojo(ListaDeseosEntity.class);
            entityManager.persist(lista);
            nuevaLista.add(lista);
        }

        outfitListaDeseosService.replaceListasDeseos(outfit.getId(), nuevaLista);
        List<ListaDeseosEntity> listas = outfitListaDeseosService.getListasDeseos(outfit.getId());
        for (ListaDeseosEntity l : nuevaLista) {
            assertTrue(listas.contains(l));
        }
    }

    /**
     * Prueba para reemplazar listas en un outfit inexistente.
     */
    @Test
    void testReplaceListasDeseosOutfitNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<ListaDeseosEntity> nuevaLista = new ArrayList<>();
            ListaDeseosEntity lista = factory.manufacturePojo(ListaDeseosEntity.class);
            entityManager.persist(lista);
            nuevaLista.add(lista);
            outfitListaDeseosService.replaceListasDeseos(0L, nuevaLista);
        });
    }

    /**
     * Prueba para eliminar una lista de deseos de un outfit.
     */
    @Test
    void testRemoveListaDeseos() throws EntityNotFoundException {
        ListaDeseosEntity lista = listaDeseosList.get(0);
        outfitListaDeseosService.removeListaDeseos(outfit.getId(), lista.getId());
        assertFalse(outfitListaDeseosService.getListasDeseos(outfit.getId()).contains(lista));
    }

    /**
     * Prueba para eliminar una lista inexistente.
     */
    @Test
    void testRemoveListaDeseosInexistente() {
        assertThrows(EntityNotFoundException.class, () -> outfitListaDeseosService.removeListaDeseos(outfit.getId(), 0L));
    }

    /**
     * Prueba para eliminar una lista de un outfit inexistente.
     */
    @Test
    void testRemoveListaDeseosOutfitNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            ListaDeseosEntity lista = listaDeseosList.get(0);
            outfitListaDeseosService.removeListaDeseos(0L, lista.getId());
        });
    }
}
