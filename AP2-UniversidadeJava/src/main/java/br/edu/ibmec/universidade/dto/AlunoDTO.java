package br.edu.ibmec.universidade.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.edu.ibmec.universidade.entity.EstadoCivil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AlunoDTO {
    private Integer matricula;
    private String nome;
    private DataNascimentoDTO dataNascimento; // {dia, mes, ano}
    private Integer idade;
    private boolean matriculaAtiva;
    private EstadoCivil estadoCivil;
    @Builder.Default
    private List<String> telefones = new ArrayList<>();
    private Integer cursoId;
    private BigDecimal bolsaPercentual;
}