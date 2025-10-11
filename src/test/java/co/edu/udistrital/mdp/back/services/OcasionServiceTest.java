package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.OcasionRepository;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(OcasionService.class)
class OcasionServiceTest {

    @Autowired
    private OcasionService ocasionService;

    @Autowired
    private OcasionRepository ocasionRepository;

    // Simulamos OutfitRepository ya que no lo necesitamos realmente para estas pruebas CRUD
    @MockBean
    private OutfitRepository outfitRepository;

    private OcasionEntity ocasion;

    @BeforeEach
    void setUp() {
        ocasion = new OcasionEntity();
        ocasion.setNombre("Cena elegante");
        ocasionRepository.save(ocasion);
    }

    // --- CREATE ---

    @Test
    void testCreateOcasion_exito() throws Exception {
        OcasionEntity nueva = new OcasionEntity();
        nueva.setNombre("Deporte");

        OcasionEntity creada = ocasionService.createOcasion(nueva);

        assertNotNull(creada.getId());
        assertEquals("Deporte", creada.getNombre());
    }

    @Test
    void testCreateOcasion_nombreVacio() {
        OcasionEntity nueva = new OcasionEntity();
        nueva.setNombre("  "); // vacío o solo espacios

        assertThrows(IllegalOperationException.class, () -> ocasionService.createOcasion(nueva));
    }

    @Test
    void testCreateOcasion_nombreDuplicado() {
        OcasionEntity duplicada = new OcasionEntity();
        duplicada.setNombre("Cena elegante"); // igual que la existente

        assertThrows(IllegalOperationException.class, () -> ocasionService.createOcasion(duplicada));
    }

    // --- READ ---

    @Test
    void testGetOcasiones_exito() {
        List<OcasionEntity> lista = ocasionService.getOcasiones();

        assertFalse(lista.isEmpty());
        assertEquals("Cena elegante", lista.get(0).getNombre());
    }

    @Test
    void testGetOcasion_exito() throws Exception {
        OcasionEntity encontrada = ocasionService.getOcasion(ocasion.getId());

        assertNotNull(encontrada);
        assertEquals("Cena elegante", encontrada.getNombre());
    }

    @Test
    void testGetOcasion_noExiste() {
        assertThrows(EntityNotFoundException.class, () -> ocasionService.getOcasion(999L));
    }

    // --- UPDATE ---

    @Test
    void testUpdateOcasion_exito() throws Exception {
        OcasionEntity cambios = new OcasionEntity();
        cambios.setNombre("Fiesta de gala");

        OcasionEntity actualizada = ocasionService.updateOcasion(ocasion.getId(), cambios);

        assertEquals("Fiesta de gala", actualizada.getNombre());
    }

    @Test
    void testUpdateOcasion_noExiste() {
        OcasionEntity cambios = new OcasionEntity();
        cambios.setNombre("Fiesta");

        assertThrows(EntityNotFoundException.class, () -> ocasionService.updateOcasion(999L, cambios));
    }

    @Test
    void testUpdateOcasion_nombreDuplicado() throws Exception {
        // Creamos otra ocasión con nombre distinto
        OcasionEntity otra = new OcasionEntity();
        otra.setNombre("Deporte");
        ocasionRepository.save(otra);

        // Intentamos actualizar la segunda con el mismo nombre de la primera
        OcasionEntity cambios = new OcasionEntity();
        cambios.setNombre("Cena elegante");

        assertThrows(IllegalOperationException.class, () -> ocasionService.updateOcasion(otra.getId(), cambios));
    }

    @Test
    void testUpdateOcasion_nombreVacio() {
        OcasionEntity cambios = new OcasionEntity();
        cambios.setNombre("  "); // vacío

        assertThrows(IllegalOperationException.class, () -> ocasionService.updateOcasion(ocasion.getId(), cambios));
    }

    // --- DELETE ---

    @Test
    void testDeleteOcasion_exito() throws Exception {
        Mockito.when(outfitRepository.existsById(ocasion.getId())).thenReturn(false);

        ocasionService.deleteOcasion(ocasion.getId());

        assertTrue(ocasionRepository.findById(ocasion.getId()).isEmpty());
    }

    @Test
    void testDeleteOcasion_noExiste() {
        assertThrows(EntityNotFoundException.class, () -> ocasionService.deleteOcasion(999L));
    }

    @Test
    void testDeleteOcasion_asociadaAOutfit() {
        Mockito.when(outfitRepository.existsById(ocasion.getId())).thenReturn(true);

        assertThrows(IllegalOperationException.class, () -> ocasionService.deleteOcasion(ocasion.getId()));
    }
}
