package br.edu.ibmec.universidade.resource;

import br.edu.ibmec.universidade.dto.CursoDTO;
import br.edu.ibmec.universidade.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
@Tag(name = "02 - Cursos", description = "Gestão de Cursos")
public class CursoResource {
    private final CursoService service;

    @GetMapping @Operation(summary = "Listar cursos")
    public ResponseEntity<?> list() {
        try { return ResponseEntity.ok(service.list()); }
        catch (Exception e) { return error(e); }
    }

    @GetMapping("/{id}") @Operation(summary = "Buscar curso por código")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        try {
            CursoDTO dto = service.get(id);
            return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (Exception e) { return error(e); }
    }

    @PostMapping @Operation(summary = "Criar curso")
    public ResponseEntity<?> create(@RequestBody CursoDTO dto) {
        try {
            CursoDTO saved = service.create(dto);
            return ResponseEntity.created(URI.create("/cursos/" + saved.getCodigo())).body(saved);
        } catch (Exception e) { return error(e); }
    }

    // @PutMapping("/{id}") @Operation(summary = "Atualizar curso")
    // public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CursoDTO dto) {
    //     try { return ResponseEntity.ok(service.update(id, dto)); }
    //     catch (Exception e) { return error(e); }
    // }

    // @DeleteMapping("/{id}") @Operation(summary = "Remover curso")
    // public ResponseEntity<?> delete(@PathVariable Integer id) {
    //     try { service.delete(id); return ResponseEntity.noContent().build(); }
    //     catch (Exception e) { return error(e); }
    // }

    private ResponseEntity<Map<String, String>> error(Exception e) {
        return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
    }
}
