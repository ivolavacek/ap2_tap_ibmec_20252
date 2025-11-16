package br.edu.ibmec.universidade.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ibmec.universidade.dto.MensalidadeDTO;
import br.edu.ibmec.universidade.entity.Aluno;
import br.edu.ibmec.universidade.entity.Inscricao;
import br.edu.ibmec.universidade.entity.Turma;
import br.edu.ibmec.universidade.repository.AlunoRepository;
import br.edu.ibmec.universidade.repository.InscricaoRepository;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class MensalidadeService {
    private final AlunoRepository repo;
    private final InscricaoRepository inscricaoRepository;

    public MensalidadeDTO calcularMensalidade(Integer matricula) {
        Aluno aluno = repo.findById(matricula)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        // 1) Buscar inscrições do aluno
        List<Inscricao> inscricoes = inscricaoRepository.findByAlunoMatricula(matricula);

        if (inscricoes.isEmpty()) {
            throw new RuntimeException("Aluno não possui turmas/disciplina inscritas.");
        }

        // 2) Filtrar inscrições ativas (se tiver esse campo)
        List<Inscricao> inscricoesAtivas = inscricoes.stream()
                .filter(Inscricao::isAtiva) // ajuste se o nome do método for outro
                .toList();

        if (inscricoesAtivas.isEmpty()) {
            throw new RuntimeException("Aluno não possui inscrições ativas.");
        }

        // 3) Somar mensalidade das disciplinas das turmas
        BigDecimal baseTotal = inscricoesAtivas.stream()
                .map(inscricao -> {
                    Turma turma = inscricao.getTurma();
                    if (turma == null || turma.getDisciplina() == null) {
                        throw new RuntimeException("Turma/Disciplina não encontrada para a inscrição " + inscricao.getId());
                    }
                    BigDecimal valorDisciplina = turma.getDisciplina().getMensalidadeBase(); // ajuste o nome do campo
                    if (valorDisciplina == null) {
                        throw new RuntimeException("Disciplina sem mensalidade definida: "
                                + turma.getDisciplina().getNome());
                    }
                    return valorDisciplina;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4) Bolsa do aluno (percentual)
        BigDecimal bolsa = aluno.getBolsaPercentual();
        if (bolsa == null) bolsa = BigDecimal.ZERO;

        BigDecimal fatorDesconto = BigDecimal.ONE.subtract(
                bolsa.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)
        );

        BigDecimal mensalidadeFinal = baseTotal.multiply(fatorDesconto)
                .setScale(2, RoundingMode.HALF_UP);

        // 5) Quantidade de turmas/disciplinas (pra você exibir se quiser)
        long qtdTurmas = inscricoesAtivas.size();
        long qtdDisciplinas = inscricoesAtivas.stream()
                .map(inscricao -> inscricao.getTurma().getDisciplina().getId())
                .distinct()
                .count();

        return MensalidadeDTO.builder()
                .matriculaAluno(aluno.getMatricula())
                .nomeAluno(aluno.getNome())
                // se quiser, pode tirar o nomeCurso, já que a mensalidade agora é por disciplina
                .mensalidadeBase(baseTotal)
                .bolsaPercentual(bolsa)
                .mensalidadeFinal(mensalidadeFinal)
                .quantidadeTurmas(qtdTurmas)           // se você criar esse campo no DTO
                .quantidadeDisciplinas(qtdDisciplinas) // idem
                .build();
    }
}