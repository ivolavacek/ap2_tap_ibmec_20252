package br.edu.ibmec.universidade.resource;

import br.edu.ibmec.universidade.dto.AlunoDTO;
import br.edu.ibmec.universidade.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
@Tag(name = "01 - Alunos", description = "Gestão de Alunos")
public class AlunoResource {
    private final AlunoService service;

    @PostMapping
    @Operation(summary = "Criar aluno")
    public ResponseEntity<?> create(@RequestBody AlunoDTO dto) {
        try {
            AlunoDTO saved = service.create(dto);
            return ResponseEntity.created(URI.create("/alunos/" + saved.getMatricula())).body(saved);
        } catch (Exception e) { return error(e); }
    }

    @GetMapping
    @Operation(summary = "Listar alunos")
    public ResponseEntity<?> list() {
        try { List<AlunoDTO> out = service.list(); return ResponseEntity.ok(out); }
        catch (Exception e) { return error(e); }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por matrícula")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        try {
            AlunoDTO dto = service.get(id);
            return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (Exception e) { return error(e); }
    }

    // @GetMapping("/{id}/mensalidade")
    // @Operation(summary = "Consultar mensalidade do aluno (já considerando bolsa)")
    // public ResponseEntity<?> mensalidade(@PathVariable Integer id) {
    //     try {
    //         MensalidadeDTO dto = service.calcularMensalidade(id);
    //         return ResponseEntity.ok(dto);
    //     } catch (Exception e) {
    //         return error(e);
    //     }
    // }

    @PutMapping("/{matricula}/curso/{cursoId}")
    @Operation(summary = "Associar aluno a um curso")
    public ResponseEntity<?> vincularCurso(
            @PathVariable Integer matricula,
            @PathVariable Integer cursoId) {
        try {
            AlunoDTO dto = service.vincularCurso(matricula, cursoId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return error(e);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody AlunoDTO dto) {
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (Exception e) { return error(e); }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover aluno")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("Não é possível deletar o aluno. Existem inscrições associadas.");
        } catch (Exception e) { return error(e); }
    }

    private ResponseEntity<Map<String, String>> error(Exception e) {
        return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
    }
}
