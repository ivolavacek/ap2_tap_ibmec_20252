package br.edu.ibmec.universidade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DisciplinaDTO {
    private Integer id;
    private String nome;
    private Integer cursoId; // relacionamento
}