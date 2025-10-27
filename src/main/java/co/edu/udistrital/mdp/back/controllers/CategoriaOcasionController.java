package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.entities.CategoriaOcasion;
import co.edu.udistrital.mdp.back.services.CategoriaOcasionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categoria-ocasion")
@RequiredArgsConstructor
public class CategoriaOcasionController {

    private final CategoriaOcasionService categoriaOcasionService;

    @GetMapping
    public ResponseEntity<List<CategoriaOcasion>> getAll() {
        return ResponseEntity.ok(categoriaOcasionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaOcasion> getById(@PathVariable Long id) {
        CategoriaOcasion entity = categoriaOcasionService.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CategoriaOcasion> create(@RequestBody CategoriaOcasion categoriaOcasion) {
        CategoriaOcasion saved = categoriaOcasionService.save(categoriaOcasion);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaOcasion> update(@PathVariable Long id, @RequestBody CategoriaOcasion categoriaOcasion) {
        CategoriaOcasion updated = categoriaOcasionService.update(id, categoriaOcasion);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = categoriaOcasionService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
