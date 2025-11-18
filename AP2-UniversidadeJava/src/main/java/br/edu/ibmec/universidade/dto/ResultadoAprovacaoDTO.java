package br.edu.ibmec.universidade.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ResultadoAprovacaoDTO {

    private Integer inscricaoId;
    private Integer alunoId;
    private String alunoNome;
    private Integer turmaId;
    private String disciplinaNome;

    private BigDecimal ap1;
    private BigDecimal ap2;
    private BigDecimal ac;
    private BigDecimal as;

    private Integer numeroFaltas;
    private Integer limiteFaltas;

    private BigDecimal notaFinal;

    private boolean aprovadoGeral;
}
