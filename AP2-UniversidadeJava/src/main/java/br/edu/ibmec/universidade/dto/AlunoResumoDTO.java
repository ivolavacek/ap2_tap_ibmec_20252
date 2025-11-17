package br.edu.ibmec.universidade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AlunoResumoDTO {
    private Integer matricula;
    private String nome;
    private Integer cursoId;
    private String nomeCurso;
}
