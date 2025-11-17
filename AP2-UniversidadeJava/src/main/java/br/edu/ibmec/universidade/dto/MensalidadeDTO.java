package br.edu.ibmec.universidade.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensalidadeDTO {

    private Integer matriculaAluno;
    private String nomeAluno;
    private String nomeCurso;
    
    private BigDecimal bolsaPercentual;
    
    private Long quantidadeDisciplinas;
    private BigDecimal mensalidadeBase;

    private BigDecimal mensalidadeFinal;
}

