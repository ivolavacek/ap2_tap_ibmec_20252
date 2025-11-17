package br.edu.ibmec.universidade.resource;

import br.edu.ibmec.universidade.dto.TurmaDTO;
import br.edu.ibmec.universidade.service.TurmaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/turmas")
@RequiredArgsConstructor
@Tag(name = "06 - Turmas", description = "Gestão de Turmas")
public class TurmaResource {
    private final TurmaService service;

    @GetMapping @Operation(summary = "Listar turmas")
    public ResponseEntity<?> list() {
        try { return ResponseEntity.ok(service.list()); }
        catch (Exception e) { return error(e); }
    }

    @GetMapping("/{id}") @Operation(summary = "Buscar turma por id")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        try {
            TurmaDTO dto = service.get(id);
            return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (Exception e) { return error(e); }
    }

    @PostMapping @Operation(summary = "Criar turma")
    public ResponseEntity<?> create(@RequestBody TurmaDTO dto) {
        try {
            TurmaDTO saved = service.create(dto);
            return ResponseEntity.created(URI.create("/turmas/" + saved.getId())).body(saved);
        } catch (Exception e) { return error(e); }
    }

    @PatchMapping("/{id}/ativa")
    @Operation(summary = "Ativar ou desativar uma turma")
    public ResponseEntity<?> atualizarStatusTurma(
            @PathVariable Integer id,
            @RequestParam boolean ativa
    ) {
        try {
            TurmaDTO dto = service.atualizarStatusTurma(id, ativa);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return error(e);
        }
    }

    // @PutMapping("/{id}") @Operation(summary = "Atualizar turma")
    // public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody TurmaDTO dto) {
    //     try { return ResponseEntity.ok(service.update(id, dto)); }
    //     catch (Exception e) { return error(e); }
    // }

    // @DeleteMapping("/{id}") @Operation(summary = "Remover turma")
    // public ResponseEntity<?> delete(@PathVariable Integer id) {
    //     try { service.delete(id); return ResponseEntity.noContent().build(); }
    //     catch (Exception e) { return error(e); }
    // }

    private ResponseEntity<Map<String, String>> error(Exception e) {
        return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
    }
}
