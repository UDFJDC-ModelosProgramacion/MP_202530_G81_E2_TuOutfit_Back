package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.RecomendacionEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.repositories.RecomendacionRepository;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(RecomendacionService.class)
class RecomendacionServiceTest {

    @Autowired
    private RecomendacionService recomendacionService;

    @Autowired
    private RecomendacionRepository recomendacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OutfitRepository outfitRepository;

    private UsuarioEntity usuario;
    private OutfitEntity outfit;
    private RecomendacionEntity recomendacion;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioEntity();
        usuario.setNombre("María");
        usuarioRepository.save(usuario);

        outfit = new OutfitEntity();
        outfit.setNombre("Outfit casual");
        outfitRepository.save(outfit);

        recomendacion = new RecomendacionEntity();
        recomendacion.setMotivo("Ideal para clima cálido");
        recomendacion.setUsuario(usuario);
        recomendacion.setOutfit(outfit);
        recomendacionRepository.save(recomendacion);
    }

    @Test
    void testCrearRecomendacion() throws Exception {
        UsuarioEntity nuevoUsuario = usuarioRepository.save(new UsuarioEntity());
        nuevoUsuario.setNombre("Usuario nuevo");

        OutfitEntity nuevoOutfit = outfitRepository.save(new OutfitEntity());
        nuevoOutfit.setNombre("Outfit oficina");

        RecomendacionEntity nueva = new RecomendacionEntity();
        nueva.setMotivo("Perfecta para oficina");
        nueva.setUsuario(nuevoUsuario);
        nueva.setOutfit(nuevoOutfit);

        RecomendacionEntity creada = recomendacionService.createRecomendacion(nueva);

        assertNotNull(creada);
        assertEquals("Perfecta para oficina", creada.getMotivo());
        assertTrue(recomendacionRepository.findById(creada.getId()).isPresent());
    }

    @Test
    void testGetRecomendaciones() {
        List<RecomendacionEntity> lista = recomendacionService.getRecomendaciones();
        assertFalse(lista.isEmpty());
        assertEquals("Ideal para clima cálido", lista.get(0).getMotivo());
    }

    @Test
    void testGetRecomendacionPorId() throws Exception {
        RecomendacionEntity encontrada = recomendacionService.getRecomendacion(recomendacion.getId());
        assertNotNull(encontrada);
        assertEquals("Ideal para clima cálido", encontrada.getMotivo());
    }

    @Test
    void testGetRecomendacionNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> recomendacionService.getRecomendacion(999L));
    }

    @Test
    void testUpdateRecomendacion() throws Exception {
        RecomendacionEntity actualizada = new RecomendacionEntity();
        actualizada.setMotivo("Actualizada para evento nocturno");
        actualizada.setUsuario(usuario);
        actualizada.setOutfit(outfit);

        RecomendacionEntity result = recomendacionService.updateRecomendacion(recomendacion.getId(), actualizada);
        assertEquals("Actualizada para evento nocturno", result.getMotivo());
    }

    @Test
    void testDeleteRecomendacion() throws Exception {
        RecomendacionEntity nueva = new RecomendacionEntity();
        nueva.setMotivo("Eliminar esta recomendación");
        nueva.setUsuario(usuario);
        nueva.setOutfit(outfit);
        recomendacionRepository.save(nueva);

        recomendacionService.deleteRecomendacion(nueva.getId(), usuario.getId());
        assertFalse(recomendacionRepository.findById(nueva.getId()).isPresent());
    }

    @Test
    void testDeleteRecomendacionNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> recomendacionService.deleteRecomendacion(888L, null));
    }
}
