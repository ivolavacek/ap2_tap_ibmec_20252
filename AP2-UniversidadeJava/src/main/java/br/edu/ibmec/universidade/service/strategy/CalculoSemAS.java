package br.edu.ibmec.universidade.service.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.edu.ibmec.universidade.entity.Inscricao;
import br.edu.ibmec.universidade.entity.Turma;

public class CalculoSemAS implements CalculadoraAprovacaoStrategy {

    private static final BigDecimal PESO_AP = new BigDecimal("0.40");
    private static final BigDecimal PESO_AC = new BigDecimal("0.20");
    private static final BigDecimal NOTA_CORTE = new BigDecimal("7.0");  // ajuste se o professor usar outro valor

    @Override
    public ResultadoAprovacao calcular(Inscricao i) {
        BigDecimal ap1 = defaultZero(i.getAp1());
        BigDecimal ap2 = defaultZero(i.getAp2());
        BigDecimal ac  = defaultZero(i.getAc());

        BigDecimal notaFinal = ap1.multiply(PESO_AP)
                .add(ap2.multiply(PESO_AP))
                .add(ac.multiply(PESO_AC))
                .setScale(2, RoundingMode.HALF_UP);

        boolean aprovadoPorNota = notaFinal.compareTo(NOTA_CORTE) >= 0;
        boolean aprovadoPorFaltas = verificaFaltas(i);
        boolean aprovadoGeral = aprovadoPorNota && aprovadoPorFaltas;

        return new ResultadoAprovacao(notaFinal, aprovadoPorNota, aprovadoPorFaltas, aprovadoGeral, false);
    }

    private BigDecimal defaultZero(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private boolean verificaFaltas(Inscricao i) {
        Turma t = i.getTurma();
        if (t == null) return true;

        Integer limiteFaltas = t.getLimiteFaltas();     // assumindo que Turma tem esse campo
        Integer faltas = i.getNumeroFaltas();

        if (limiteFaltas == null || faltas == null) return true;

        return faltas <= limiteFaltas;
    }
}