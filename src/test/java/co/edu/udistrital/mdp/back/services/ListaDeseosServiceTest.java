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
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ListaDeseosService.class)
class ListaDeseosServiceTest {

    @Autowired
    private ListaDeseosService listaDeseosService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ListaDeseosEntity> listaDeseosList = new ArrayList<>();
    private UsuarioEntity usuario;

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
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        usuario = factory.manufacturePojo(UsuarioEntity.class);
        usuario.setCorreo("user@test.com");
        usuario.setNombre("Laura Gómez");
        entityManager.persist(usuario);

        ListaDeseosEntity lista = factory.manufacturePojo(ListaDeseosEntity.class);
        lista.setUsuario(usuario);
        lista.setOutfits(new ArrayList<>());
        entityManager.persist(lista);
        listaDeseosList.add(lista);
    }

    /**
     * Prueba para crear una lista de deseos.
     */
    @Test
    void testCreateListaDeseosCorrecto() throws EntityNotFoundException {
        UsuarioEntity nuevoUsuario = factory.manufacturePojo(UsuarioEntity.class);
        nuevoUsuario.setCorreo("nuevo@test.com");
        nuevoUsuario.setNombre("Nuevo Usuario");
        entityManager.persist(nuevoUsuario);

        ListaDeseosEntity toCreate = factory.manufacturePojo(ListaDeseosEntity.class);
        ListaDeseosEntity result = listaDeseosService.createListaDeseos(nuevoUsuario.getId(), toCreate);

        assertNotNull(result);
        assertEquals(nuevoUsuario.getId(), result.getUsuario().getId());
    }

    /**
     * Prueba para crear una lista de deseos con usuario inexistente.
     */
    @Test
    void testCreateListaDeseosUsuarioNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            ListaDeseosEntity toCreate = factory.manufacturePojo(ListaDeseosEntity.class);
            listaDeseosService.createListaDeseos(0L, toCreate);
        });
    }

    /**
     * Prueba para consultar todas las listas de deseos.
     */
    @Test
    void testGetListaDeseos() {
        List<ListaDeseosEntity> lista = listaDeseosService.getListaDeseos();
        assertEquals(listaDeseosList.size(), lista.size());
    }

    /**
     * Prueba para consultar una lista de deseos existente.
     */
    @Test
    void testGetListaDeseosExistente() throws EntityNotFoundException {
        ListaDeseosEntity entity = listaDeseosList.get(0);
        ListaDeseosEntity result = listaDeseosService.getListaDeseos(entity.getId());
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    /**
     * Prueba para consultar una lista de deseos inexistente.
     */
    @Test
    void testGetListaDeseosNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> listaDeseosService.getListaDeseos(0L));
    }

    /**
     * Prueba para actualizar una lista de deseos.
     */
    @Test
    void testUpdateListaDeseosCorrecto() throws EntityNotFoundException {
        ListaDeseosEntity entity = listaDeseosList.get(0);
        ListaDeseosEntity update = factory.manufacturePojo(ListaDeseosEntity.class);
        ListaDeseosEntity result = listaDeseosService.updateListaDeseos(entity.getId(), update);
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    /**
     * Prueba para actualizar una lista de deseos inexistente.
     */
    @Test
    void testUpdateListaDeseosNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            ListaDeseosEntity update = factory.manufacturePojo(ListaDeseosEntity.class);
            listaDeseosService.updateListaDeseos(0L, update);
        });
    }

    /**
     * Prueba para eliminar una lista de deseos.
     */
    @Test
    void testDeleteListaDeseosCorrecto() throws EntityNotFoundException {
        ListaDeseosEntity entity = listaDeseosList.get(0);
        listaDeseosService.deleteListaDeseos(entity.getId());
        assertNull(entityManager.find(ListaDeseosEntity.class, entity.getId()));
    }

    /**
     * Prueba para eliminar una lista de deseos inexistente.
     */
    @Test
    void testDeleteListaDeseosNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> listaDeseosService.deleteListaDeseos(0L));
    }

    /**
     * Prueba para agregar un outfit a la lista de deseos.
     */
    @Test
    void testAddOutfitCorrecto() throws EntityNotFoundException, IllegalOperationException {
        ListaDeseosEntity lista = listaDeseosList.get(0);
        OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(outfit);

        OutfitEntity result = listaDeseosService.addOutfit(lista.getId(), outfit.getId());
        assertNotNull(result);
        assertTrue(lista.getOutfits().contains(outfit));
    }

    /**
     * Prueba para agregar un outfit repetido.
     */
    @Test
    void testAddOutfitRepetido() {
        assertThrows(IllegalOperationException.class, () -> {
            ListaDeseosEntity lista = listaDeseosList.get(0);
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfit);
            lista.getOutfits().add(outfit);
            entityManager.persist(lista);

            listaDeseosService.addOutfit(lista.getId(), outfit.getId());
        });
    }

    /**
     * Prueba para consultar los outfits de una lista.
     */
    @Test
    void testGetOutfits() throws EntityNotFoundException {
        ListaDeseosEntity lista = listaDeseosList.get(0);
        OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(outfit);
        lista.getOutfits().add(outfit);
        entityManager.persist(lista);

        List<OutfitEntity> outfits = listaDeseosService.getOutfits(lista.getId());
        assertEquals(1, outfits.size());
    }

    /**
     * Prueba para consultar los outfits de una lista inexistente.
     */
    @Test
    void testGetOutfitsListaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> listaDeseosService.getOutfits(0L));
    }
}
