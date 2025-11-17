package br.edu.ibmec.universidade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InscricaoAlunoDTO {
    private Integer id;              // ajuste p/ tipo do ID de Inscricao se for Integer
    private Integer turmaId;
    private Boolean turmaAtiva;
    private Integer semestre;
    private Integer ano;

    private Integer disciplinaId;
    private String disciplinaNome;

    private Integer cursoId;
    private String cursoNome;
}
