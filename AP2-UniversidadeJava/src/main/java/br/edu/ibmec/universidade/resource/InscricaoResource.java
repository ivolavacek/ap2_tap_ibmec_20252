package br.edu.ibmec.universidade.resource;

import br.edu.ibmec.universidade.dto.InscricaoDTO;
import br.edu.ibmec.universidade.service.InscricaoService;
import br.edu.ibmec.universidade.dto.ResultadoAprovacaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/inscricoes")
@RequiredArgsConstructor
@Tag(name = "03 - Inscrições", description = "Gestão de Inscrições")
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

    @GetMapping("/{id}/resultado")
    @Operation(summary = "Calcular resultado (nota e aprovação) de uma inscrição")
    public ResponseEntity<?> resultado(@PathVariable Integer id) {
        try {
            ResultadoAprovacaoDTO dto = service.calcularResultado(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return error(e);
        }
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

    @PatchMapping("/{id}/ap1")
    @Operation(summary = "Atualizar nota AP1 da inscrição")
    public ResponseEntity<?> atualizarAp1(
            @PathVariable Integer id,
            @RequestParam BigDecimal nota) {
        try {
            InscricaoDTO dto = service.atualizarAp1(id, nota);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return error(e);
        }
    }

    @PatchMapping("/{id}/ap2")
    @Operation(summary = "Atualizar nota AP2 da inscrição")
    public ResponseEntity<?> atualizarAp2(
            @PathVariable Integer id,
            @RequestParam BigDecimal nota) {
        try {
            InscricaoDTO dto = service.atualizarAp2(id, nota);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return error(e);
        }
    }

    @PatchMapping("/{id}/ac")
    @Operation(summary = "Atualizar nota AC da inscrição")
    public ResponseEntity<?> atualizarAc(
            @PathVariable Integer id,
            @RequestParam BigDecimal nota) {
        try {
            InscricaoDTO dto = service.atualizarAc(id, nota);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return error(e);
        }
    }

    @PatchMapping("/{id}/as")
    @Operation(summary = "Atualizar nota AS da inscrição")
    public ResponseEntity<?> atualizarAs(
            @PathVariable Integer id,
            @RequestParam BigDecimal nota) {
        try {
            InscricaoDTO dto = service.atualizarAs(id, nota);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return error(e);
        }
    }

    @PatchMapping("/{id}/faltas")
    @Operation(summary = "Adicionar faltas à inscrição")
    public ResponseEntity<?> adicionarFaltas(
            @PathVariable Integer id,
            @RequestParam Integer faltas) {
        try {
            InscricaoDTO dto = service.adicionarFaltas(id, faltas);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return error(e);
        }
    }

    @DeleteMapping("/{id}") @Operation(summary = "Remover inscrição")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (Exception e) { return error(e); }
    }

    private ResponseEntity<Map<String, String>> error(Exception e) {
        return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
    }
}
