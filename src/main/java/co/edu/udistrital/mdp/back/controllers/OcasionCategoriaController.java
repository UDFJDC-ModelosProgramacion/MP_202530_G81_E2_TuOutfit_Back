package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.services.OcasionCategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/ocasion")
public class OcasionCategoriaController {
@Autowired
private OcasionCategoriaService ocasionCategoriaService;

    @GetMapping("/{ocasionId}/categorias")
    public ResponseEntity<List<CategoriaEntity>> getCategoriasPorOcasion(@PathVariable Long ocasionId) throws EntityNotFoundException {
        List<CategoriaEntity> categorias = ocasionCategoriaService.getCategoriasPorOcasion(ocasionId);
        return ResponseEntity.ok(categorias);
    }


    @PostMapping("/{ocasionId}/categorias/{categoriaId}")
    public ResponseEntity<CategoriaEntity> addCategoriaAOcasion(@PathVariable Long ocasionId,
                                                                @PathVariable Long categoriaId) throws EntityNotFoundException {
        CategoriaEntity categoria = ocasionCategoriaService.addCategoriaAOcasion(ocasionId, categoriaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    
    @DeleteMapping("/{ocasionId}/categorias/{categoriaId}")
    public ResponseEntity<Void> removeCategoriaDeOcasion(@PathVariable Long ocasionId,
                                                         @PathVariable Long categoriaId) throws EntityNotFoundException {
        ocasionCategoriaService.removeCategoriaDeOcasion(ocasionId, categoriaId);
        return ResponseEntity.noContent().build();
    }
}
