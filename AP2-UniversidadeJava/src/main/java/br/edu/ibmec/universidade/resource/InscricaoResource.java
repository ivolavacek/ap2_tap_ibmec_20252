package br.edu.ibmec.universidade.resource;

import br.edu.ibmec.universidade.dto.InscricaoDTO;
import br.edu.ibmec.universidade.service.InscricaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/inscricoes")
@RequiredArgsConstructor
@Tag(name = "06 - Inscrições", description = "Gestão de Inscrições")
public class InscricaoResource {
    private final InscricaoService service;

    @GetMapping @Operation(summary = "Listar inscrições")
    public ResponseEntity<?> list() {
        try { return ResponseEntity.ok(service.list()); }
        catch (Exception e) { return error(e); }
    }

    @GetMapping("/{id}") @Operation(summary = "Buscar inscrição por id")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        try {
            InscricaoDTO dto = service.get(id);
            return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (Exception e) { return error(e); }
    }

    @PostMapping @Operation(summary = "Criar inscrição")
    public ResponseEntity<?> create(@RequestBody InscricaoDTO dto) {
        try {
            InscricaoDTO saved = service.create(dto);
            return ResponseEntity.created(URI.create("/inscricoes/" + saved.getId())).body(saved);
        } catch (Exception e) { return error(e); }
    }

    // @PutMapping("/{id}") @Operation(summary = "Atualizar inscrição")
    // public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody InscricaoDTO dto) {
    //     try { return ResponseEntity.ok(service.update(id, dto)); }
    //     catch (Exception e) { return error(e); }
    // }

    @DeleteMapping("/{id}") @Operation(summary = "Remover inscrição")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (Exception e) { return error(e); }
    }

    private ResponseEntity<Map<String, String>> error(Exception e) {
        return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
    }
}
