package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
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
import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MarcaService.class)
class MarcaServiceTest {

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MarcaEntity> marcaList = new ArrayList<>();
    private List<TiendaEntity> tiendaList = new ArrayList<>();

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
        entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MarcaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TiendaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TiendaEntity tienda = factory.manufacturePojo(TiendaEntity.class);
            entityManager.persist(tienda);
            tiendaList.add(tienda);
        }

        for (int i = 0; i < 3; i++) {
            MarcaEntity marca = factory.manufacturePojo(MarcaEntity.class);
            marca.setNombre("Marca" + i);
            marca.setTiendas(Collections.singletonList(tiendaList.get(i % tiendaList.size())));
            entityManager.persist(marca);
            marcaList.add(marca);
        }
    }

    /**
     * Prueba para crear una marca correctamente.
     */
    @Test
    void testCreateMarcaCorrecta() throws EntityNotFoundException, IllegalOperationException {
        MarcaEntity nueva = factory.manufacturePojo(MarcaEntity.class);
        nueva.setNombre("Marca nueva");
        nueva.setTiendas(Collections.singletonList(tiendaList.get(0)));

        MarcaEntity result = marcaService.createMarca(nueva);
        assertNotNull(result);
        assertEquals("Marca nueva", result.getNombre());
    }

    /**
     * Prueba para crear una marca sin tiendas asociadas.
     */
    @Test
    void testCreateMarcaSinTiendas() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity nueva = factory.manufacturePojo(MarcaEntity.class);
            nueva.setNombre("Marca sin tienda");
            nueva.setTiendas(Collections.emptyList());
            marcaService.createMarca(nueva);
        });
    }

    /**
     * Prueba para crear una marca con una tienda inexistente.
     */
    @Test
    void testCreateMarcaConTiendaInexistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            TiendaEntity tiendaFalsa = factory.manufacturePojo(TiendaEntity.class);
            tiendaFalsa.setId(999L);

            MarcaEntity nueva = factory.manufacturePojo(MarcaEntity.class);
            nueva.setNombre("Marca tienda inexistente");
            nueva.setTiendas(Collections.singletonList(tiendaFalsa));

            marcaService.createMarca(nueva);
        });
    }

    /**
     * Prueba para crear una marca con nombre repetido.
     */
    @Test
    void testCreateMarcaNombreRepetido() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity repetida = factory.manufacturePojo(MarcaEntity.class);
            repetida.setNombre(marcaList.get(0).getNombre());
            repetida.setTiendas(Collections.singletonList(tiendaList.get(0)));

            marcaService.createMarca(repetida);
        });
    }

    /**
     * Prueba para obtener todas las marcas.
     */
    @Test
    void testGetAllMarcas() {
        List<MarcaEntity> result = marcaService.getAllMarcas();
        assertEquals(marcaList.size(), result.size());
    }

    /**
     * Prueba para obtener una marca existente por ID.
     */
    @Test
    void testGetMarcaByIdExistente() throws EntityNotFoundException {
        MarcaEntity marca = marcaList.get(0);
        MarcaEntity result = marcaService.getMarcaById(marca.getId());
        assertEquals(marca.getNombre(), result.getNombre());
    }

    /**
     * Prueba para obtener una marca que no existe.
     */
    @Test
    void testGetMarcaByIdNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> marcaService.getMarcaById(0L));
    }

    /**
     * Prueba para actualizar una marca correctamente.
     */
    @Test
    void testUpdateMarcaCorrecta() throws EntityNotFoundException, IllegalOperationException {
        MarcaEntity marca = marcaList.get(0);
        MarcaEntity cambios = factory.manufacturePojo(MarcaEntity.class);
        cambios.setNombre("Marca actualizada");

        List<TiendaEntity> nuevasTiendas = new ArrayList<>();
        nuevasTiendas.add(tiendaList.get(1));
        cambios.setTiendas(nuevasTiendas);

        MarcaEntity result = marcaService.updateMarca(marca.getId(), cambios);
        assertEquals("Marca actualizada", result.getNombre());
    }


    /**
     * Prueba para actualizar una marca con tiendas vacías.
     */
    @Test
    void testUpdateMarcaSinTiendas() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity marca = marcaList.get(0);
            MarcaEntity cambios = factory.manufacturePojo(MarcaEntity.class);
            cambios.setNombre("Sin tiendas");
            cambios.setTiendas(Collections.emptyList());

            marcaService.updateMarca(marca.getId(), cambios);
        });
    }

    /**
     * Prueba para actualizar una marca con tienda inexistente.
     */
    @Test
    void testUpdateMarcaTiendaInexistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            MarcaEntity marca = marcaList.get(0);
            TiendaEntity tiendaFalsa = factory.manufacturePojo(TiendaEntity.class);
            tiendaFalsa.setId(999L);

            MarcaEntity cambios = factory.manufacturePojo(MarcaEntity.class);
            cambios.setNombre("Marca falsa");
            cambios.setTiendas(Collections.singletonList(tiendaFalsa));

            marcaService.updateMarca(marca.getId(), cambios);
        });
    }

    /**
     * Prueba para actualizar una marca con nombre repetido.
     */
    @Test
    void testUpdateMarcaNombreRepetido() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity marca = marcaList.get(0);
            MarcaEntity otra = marcaList.get(1);

            MarcaEntity cambios = factory.manufacturePojo(MarcaEntity.class);
            cambios.setNombre(otra.getNombre());
            cambios.setTiendas(Collections.singletonList(tiendaList.get(0)));

            marcaService.updateMarca(marca.getId(), cambios);
        });
    }

    /**
     * Prueba para eliminar una marca correctamente.
     */
    @Test
    void testDeleteMarcaCorrecta() throws EntityNotFoundException, IllegalOperationException {
        MarcaEntity marca = marcaList.get(0);
        marcaService.deleteMarca(marca.getId());
        assertNull(entityManager.find(MarcaEntity.class, marca.getId()));
    }

    /**
     * Prueba para eliminar una marca con prendas asociadas.
     */
    @Test
    void testDeleteMarcaConPrendas() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity marca = marcaList.get(1);
            PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
            prenda.setMarca(marca);
            marca.setPrendas(Collections.singletonList(prenda));

            entityManager.persist(prenda);
            entityManager.persist(marca);

            marcaService.deleteMarca(marca.getId());
        });
    }

    /**
     * Prueba para eliminar una marca que no existe.
     */
    @Test
    void testDeleteMarcaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> marcaService.deleteMarca(0L));
    }
}
