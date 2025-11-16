package br.edu.ibmec.universidade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InscricaoDTO {
    private Integer id;
    private Integer alunoId; // matricula do aluno
    private Integer turmaId;
    private Integer ano;
    private Integer semestre;
}