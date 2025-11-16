package br.edu.ibmec.universidade.resource;

import br.edu.ibmec.universidade.dto.MensalidadeDTO;
import br.edu.ibmec.universidade.service.MensalidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Se você usa Lombok:
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mensalidades")
@RequiredArgsConstructor
@Tag(name = "02 - Mensalidade", description = "Cálculo da Mensalidade do Aluno")
public class MensalidadeResource {

    private final MensalidadeService mensalidadeService;

    @GetMapping("/{matricula}")
    @Operation(summary = "Calcula a mensalidade do aluno a partir da matrícula")
    public ResponseEntity<MensalidadeDTO> calcularMensalidade(@PathVariable Integer matricula) {
        MensalidadeDTO dto = mensalidadeService.calcularMensalidade(matricula);
        return ResponseEntity.ok(dto);
    }
}
