package br.edu.ibmec.universidade.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TurmaDTO {
    private Integer id;
    private Integer disciplinaId; // relacionamento
    private String disciplinaNome; // campo adicional para exibição
    private Integer professorId;  // relacionamento
    private String professorNome;  // campo adicional para exibição

    @Min(value = 2025, message = "Ano deve ser maior ou igual a 2025")
    private Integer ano;

    @Min(value = 1, message = "Semestre deve ser 1 ou 2")
    @Max(value = 2, message = "Semestre deve ser 1 ou 2")
    private Integer semestre;

    private Integer limiteFaltas;
    private Boolean ativa;
}