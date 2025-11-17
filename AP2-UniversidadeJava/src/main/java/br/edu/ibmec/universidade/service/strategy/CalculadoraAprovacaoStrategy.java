package br.edu.ibmec.universidade.service.strategy;

import java.math.BigDecimal;
import br.edu.ibmec.universidade.entity.Inscricao;

public interface CalculadoraAprovacaoStrategy {

    ResultadoAprovacao calcular(Inscricao inscricao);

    record ResultadoAprovacao(
            BigDecimal notaFinal,
            boolean aprovadoPorNota,
            boolean aprovadoPorFaltas,
            boolean aprovadoGeral,
            boolean usouAS
    ) {}
}