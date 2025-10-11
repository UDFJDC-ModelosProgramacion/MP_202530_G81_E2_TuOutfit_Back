package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.ColorEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.repositories.ColorRepository;
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
@Import(ColorService.class)
class ColorServiceTest {

    @Autowired
    private ColorService colorService;

    @Autowired
    private ColorRepository colorRepository;

    private ColorEntity color;

    @BeforeEach
    void setUp() {
        color = new ColorEntity();
        color.setNombre("Rojo");
        colorRepository.save(color);
    }

    @Test
    void testCreateColor() throws Exception {
        ColorEntity nuevo = new ColorEntity();
        nuevo.setNombre("Azul");

        ColorEntity creado = colorService.createColor(nuevo);

        assertNotNull(creado);
        assertEquals("Azul", creado.getNombre());
        assertTrue(colorRepository.findById(creado.getId()).isPresent());
    }

    @Test
    void testGetColores() {
        List<ColorEntity> colores = colorService.getColores();

        assertFalse(colores.isEmpty());
        assertEquals("Rojo", colores.get(0).getNombre());
    }

    @Test
    void testGetColorPorId() throws Exception {
        ColorEntity encontrado = colorService.getColor(color.getId());

        assertNotNull(encontrado);
        assertEquals("Rojo", encontrado.getNombre());
    }

    @Test
    void testGetColorNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> colorService.getColor(999L));
    }

    @Test
    void testUpdateColor() throws Exception {
        ColorEntity actualizado = new ColorEntity();
        actualizado.setNombre("Verde");

        ColorEntity result = colorService.updateColor(color.getId(), actualizado);

        assertEquals("Verde", result.getNombre());
        assertEquals(color.getId(), result.getId());
    }

    @Test
    void testUpdateColorNoExistente() {
        ColorEntity actualizado = new ColorEntity();
        actualizado.setNombre("Amarillo");

        assertThrows(EntityNotFoundException.class, () -> colorService.updateColor(888L, actualizado));
    }

    @Test
    void testDeleteColor() throws Exception {
        ColorEntity nuevo = new ColorEntity();
        nuevo.setNombre("Gris");
        colorRepository.save(nuevo);

        colorService.deleteColor(nuevo.getId());

        assertFalse(colorRepository.findById(nuevo.getId()).isPresent());
    }

    @Test
    void testDeleteColorNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> colorService.deleteColor(777L));
    }
}
