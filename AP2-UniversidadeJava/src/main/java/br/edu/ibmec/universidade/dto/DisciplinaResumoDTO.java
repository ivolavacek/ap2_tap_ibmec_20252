package br.edu.ibmec.universidade.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class DisciplinaResumoDTO {
    private Integer id;
    private String nome;
}