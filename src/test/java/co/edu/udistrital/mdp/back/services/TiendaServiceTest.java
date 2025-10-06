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

import co.edu.udistrital.mdp.back.entities.MarcaEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(TiendaService.class)
class TiendaServiceTest {

    @Autowired
    private TiendaService tiendaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<TiendaEntity> tiendaList = new ArrayList<>();
    private List<MarcaEntity> marcaList = new ArrayList<>();

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
        entityManager.getEntityManager().createQuery("delete from TiendaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MarcaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        // Crear marcas
        for (int i = 0; i < 3; i++) {
            MarcaEntity marca = factory.manufacturePojo(MarcaEntity.class);
            if (marca.getNombre() == null) marca.setNombre("Marca " + i);
            entityManager.persist(marca);
            marcaList.add(marca);
        }

        // Crear tiendas asociadas a las marcas
        for (int i = 0; i < 3; i++) {
            TiendaEntity tienda = factory.manufacturePojo(TiendaEntity.class);
            if (tienda.getNombre() == null) tienda.setNombre("Tienda " + i);
            if (tienda.getUbicacion() == null) tienda.setUbicacion("Ubicacion " + i);
            tienda.setMarca(marcaList.get(i)); // asignar marca
            entityManager.persist(tienda);
            tiendaList.add(tienda);
        }
    }

    /**
     * Prueba para crear una tienda correctamente.
     * @throws IllegalOperationException
     */
    @Test
    void testCreateTiendaCorrecta() throws IllegalOperationException {
        MarcaEntity marca = factory.manufacturePojo(MarcaEntity.class);
        if (marca.getNombre() == null) marca.setNombre("Marca Nueva");
        entityManager.persist(marca);

        TiendaEntity toCreate = factory.manufacturePojo(TiendaEntity.class);
        toCreate.setNombre("Tienda Nueva");
        toCreate.setUbicacion("Ubicacion Nueva");
        toCreate.setMarca(marca);

        TiendaEntity result = tiendaService.createTienda(toCreate);

        assertNotNull(result);
        assertEquals("Tienda Nueva", result.getNombre());
        assertEquals("Ubicacion Nueva", result.getUbicacion());
        assertEquals(marca.getId(), result.getMarca().getId());
    }

    /**
     * Prueba para crear una tienda sin nombre.
     */
    @Test
    void testCreateTiendaSinNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity marca = factory.manufacturePojo(MarcaEntity.class);
            entityManager.persist(marca);

            TiendaEntity toCreate = factory.manufacturePojo(TiendaEntity.class);
            toCreate.setNombre("");
            toCreate.setUbicacion("Ubicacion");
            toCreate.setMarca(marca);

            tiendaService.createTienda(toCreate);
        });
    }

    /**
     * Prueba para crear una tienda sin marca.
     */
    @Test
    void testCreateTiendaSinMarca() {
        assertThrows(IllegalOperationException.class, () -> {
            TiendaEntity toCreate = factory.manufacturePojo(TiendaEntity.class);
            toCreate.setNombre("Tienda");
            toCreate.setUbicacion("Ubicacion");
            toCreate.setMarca(null);

            tiendaService.createTienda(toCreate);
        });
    }

    /**
     * Prueba para crear una tienda sin ubicación.
     */
    @Test
    void testCreateTiendaSinUbicacion() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity marca = factory.manufacturePojo(MarcaEntity.class);
            entityManager.persist(marca);

            TiendaEntity toCreate = factory.manufacturePojo(TiendaEntity.class);
            toCreate.setNombre("Tienda");
            toCreate.setUbicacion(null);
            toCreate.setMarca(marca);

            tiendaService.createTienda(toCreate);
        });
    }

    /**
     * Prueba para actualizar una tienda correctamente.
     */
    @Test
    void testUpdateTiendaCorrecta() throws EntityNotFoundException, IllegalOperationException {
        TiendaEntity tienda = tiendaList.get(0);
        MarcaEntity nuevaMarca = factory.manufacturePojo(MarcaEntity.class);
        entityManager.persist(nuevaMarca);

        TiendaEntity update = factory.manufacturePojo(TiendaEntity.class);
        update.setNombre("Tienda Actualizada");
        update.setUbicacion("Ubicacion Actualizada");
        update.setMarca(nuevaMarca);

        TiendaEntity result = tiendaService.updateTienda(tienda.getId(), update);

        assertEquals("Tienda Actualizada", result.getNombre());
        assertEquals("Ubicacion Actualizada", result.getUbicacion());
        assertEquals(nuevaMarca.getId(), result.getMarca().getId());
    }

    /**
     * Prueba para eliminar una tienda correctamente.
     */
    @Test
    void testDeleteTiendaCorrecta() throws EntityNotFoundException {
        TiendaEntity tienda = tiendaList.get(0);
        tiendaService.deleteTienda(tienda.getId());
        assertNull(entityManager.find(TiendaEntity.class, tienda.getId()));
    }

    /**
     * Prueba para eliminar una tienda que no existe.
     */
    @Test
    void testDeleteTiendaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> tiendaService.deleteTienda(0L));
    }
}
