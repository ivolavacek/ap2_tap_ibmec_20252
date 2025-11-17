package br.edu.ibmec.universidade.resource;

import br.edu.ibmec.universidade.dto.DisciplinaDTO;
import br.edu.ibmec.universidade.service.DisciplinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/disciplinas")
@RequiredArgsConstructor
@Tag(name = "04 - Disciplinas", description = "Gestão de Disciplinas")
public class DisciplinaResource {
    private final DisciplinaService service;

    @GetMapping @Operation(summary = "Listar disciplinas")
    public ResponseEntity<?> list() {
        try { return ResponseEntity.ok(service.list()); }
        catch (Exception e) { return error(e); }
    }

    @GetMapping("/{id}") @Operation(summary = "Buscar disciplina por id")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        try {
            DisciplinaDTO dto = service.get(id);
            return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (Exception e) { return error(e); }
    }

    @PostMapping @Operation(summary = "Criar disciplina")
    public ResponseEntity<?> create(@RequestBody DisciplinaDTO dto) {
        try {
            DisciplinaDTO saved = service.create(dto);
            return ResponseEntity.created(URI.create("/disciplinas/" + saved.getId())).body(saved);
            // return ResponseEntity.created(URI.create("/disciplinas/" + saved.getId())).body(saved);
        } catch (Exception e) { return error(e); }
    }

    // @PutMapping("/{id}") @Operation(summary = "Atualizar disciplina")
    // public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody DisciplinaDTO dto) {
    //     try { return ResponseEntity.ok(service.update(id, dto)); }
    //     catch (Exception e) { return error(e); }
    // }

    // @DeleteMapping("/{id}") @Operation(summary = "Remover disciplina")
    // public ResponseEntity<?> delete(@PathVariable Integer id) {
    //     try { service.delete(id); return ResponseEntity.noContent().build(); }
    //     catch (Exception e) { return error(e); }
    // }

    private ResponseEntity<Map<String, String>> error(Exception e) {
        return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
    }
}
