package br.edu.ibmec.universidade.resource;

import br.edu.ibmec.universidade.dto.ProfessorDTO;
import br.edu.ibmec.universidade.service.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/professores")
@RequiredArgsConstructor
@Tag(name = "05 - Professores", description = "Gestão de Professores")
public class ProfessorResource {
    private final ProfessorService service;

    @GetMapping @Operation(summary = "Listar professores")
    public ResponseEntity<?> list() {
        try { return ResponseEntity.ok(service.list()); }
        catch (Exception e) { return error(e); }
    }

    @GetMapping("/{id}") @Operation(summary = "Buscar professor por id")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        try {
            ProfessorDTO dto = service.get(id);
            return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (Exception e) { return error(e); }
    }

    @PostMapping @Operation(summary = "Criar professor")
    public ResponseEntity<?> create(@RequestBody ProfessorDTO dto) {
        try {
            ProfessorDTO saved = service.create(dto);
            return ResponseEntity.created(URI.create("/professores/" + saved.getId())).body(saved);
        } catch (Exception e) { return error(e); }
    }

    // @PutMapping("/{id}") @Operation(summary = "Atualizar professor")
    // public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ProfessorDTO dto) {
    //     try { return ResponseEntity.ok(service.update(id, dto)); }
    //     catch (Exception e) { return error(e); }
    // }

    // @DeleteMapping("/{id}") @Operation(summary = "Remover professor")
    // public ResponseEntity<?> delete(@PathVariable Integer id) {
    //     try { service.delete(id); return ResponseEntity.noContent().build(); }
    //     catch (Exception e) { return error(e); }
    // }

    private ResponseEntity<Map<String, String>> error(Exception e) {
        return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
    }
}
