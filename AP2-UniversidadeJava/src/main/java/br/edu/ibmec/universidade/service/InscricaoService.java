package br.edu.ibmec.universidade.service;

import br.edu.ibmec.universidade.dto.InscricaoDTO;
import br.edu.ibmec.universidade.entity.Aluno;
import br.edu.ibmec.universidade.entity.Inscricao;
import br.edu.ibmec.universidade.entity.Turma;
import br.edu.ibmec.universidade.repository.AlunoRepository;
import br.edu.ibmec.universidade.repository.InscricaoRepository;
import br.edu.ibmec.universidade.repository.TurmaRepository;
import br.edu.ibmec.universidade.dto.ResultadoAprovacaoDTO;
import br.edu.ibmec.universidade.service.strategy.CalculadoraAprovacaoStrategy;
import br.edu.ibmec.universidade.service.strategy.CalculoSemAS;
import br.edu.ibmec.universidade.service.strategy.CalculoComAS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class InscricaoService {
    private final InscricaoRepository repo;
    private final AlunoRepository alunoRepo;
    private final TurmaRepository turmaRepo;

    private final CalculadoraAprovacaoStrategy calculoSemAS = new CalculoSemAS();
    private final CalculadoraAprovacaoStrategy calculoComAS = new CalculoComAS();

    public List<InscricaoDTO> list() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public InscricaoDTO get(Integer id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public InscricaoDTO create(InscricaoDTO dto) {
        Inscricao i = fromDTO(dto);
        return toDTO(repo.save(i));
    }

    public InscricaoDTO update(Integer id, InscricaoDTO dto) {
        Inscricao existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        if (dto.getAlunoId() != null) {
            Aluno a = alunoRepo.findById(dto.getAlunoId())
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
            existing.setAluno(a);
        }
        if (dto.getTurmaId() != null) {
            Turma t = turmaRepo.findById(dto.getTurmaId())
                    .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
            existing.setTurma(t);
        }
        return toDTO(repo.save(existing));
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

    public InscricaoDTO atualizarAp1(Integer id, BigDecimal nota) {
        validarNota(nota);
        Inscricao i = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        i.setAp1(nota);
        return toDTO(repo.save(i));
    }

    public InscricaoDTO atualizarAp2(Integer id, BigDecimal nota) {
        validarNota(nota);
        Inscricao i = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        i.setAp2(nota);
        return toDTO(repo.save(i));
    }

    public InscricaoDTO atualizarAc(Integer id, BigDecimal nota) {
        validarNota(nota);
        Inscricao i = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        i.setAc(nota);
        return toDTO(repo.save(i));
    }

    public InscricaoDTO atualizarAs(Integer id, BigDecimal nota) {
        validarNota(nota);
        Inscricao i = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        i.setAs(nota);
        return toDTO(repo.save(i));
    }

    public InscricaoDTO adicionarFaltas(Integer id, Integer faltas) {
        Inscricao i = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));

        Integer atual = i.getNumeroFaltas() == null ? 0 : i.getNumeroFaltas();

        validarQuantidadeFaltas(atual + faltas);
        i.setNumeroFaltas(atual + faltas);

        return toDTO(repo.save(i));
    }

    private void validarNota(BigDecimal nota) {
        if (nota == null) {
            throw new IllegalArgumentException("Nota não pode ser nula.");
        }
        if (nota.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Nota não pode ser menor que 0.");
        }
        if (nota.compareTo(new BigDecimal("10")) > 0) {
            throw new IllegalArgumentException("Nota não pode ser maior que 10.");
        }
    }

    private void validarQuantidadeFaltas(Integer faltas) {
        if (faltas == null) {
            throw new IllegalArgumentException("Quantidade de faltas não pode ser nula.");
        }
        if (faltas < 0) {
            throw new IllegalArgumentException("Quantidade de faltas não pode ser negativa.");
        }
    }

    private InscricaoDTO toDTO(Inscricao i) {
        Integer alunoId = i.getAluno() == null ? null : i.getAluno().getMatricula();
        Integer turmaId    = i.getTurma() == null ? null : i.getTurma().getId();
        return InscricaoDTO.builder()
                .id(i.getId())
                .alunoId(alunoId)
                .turmaId(turmaId)
                .ap1(i.getAp1())
                .ap2(i.getAp2())
                .ac(i.getAc())
                .as(i.getAs())
                .numeroFaltas(i.getNumeroFaltas())
                .build();
    }

    private Inscricao fromDTO(InscricaoDTO dto) {
        Inscricao i = new Inscricao();
        // i.setId(dto.getId());
        if (dto.getAlunoId() != null) {
            Aluno a = alunoRepo.findById(dto.getAlunoId())
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
            i.setAluno(a);
        }
        if (dto.getTurmaId() != null) {
            Turma t = turmaRepo.findById(dto.getTurmaId())
                    .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
            i.setTurma(t);
        }
        return i;
    }

    public ResultadoAprovacaoDTO calcularResultado(Integer inscricaoId) {

        Inscricao insc = repo.findById(inscricaoId)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));

        // escolher strategy: se tem AS calculamos com AS
        CalculadoraAprovacaoStrategy strategy =
                (insc.getAs() == null ? calculoSemAS : calculoComAS);

        // Resultado do Strategy
        var r = strategy.calcular(insc);

        // Dados de faltas
        Turma turma = insc.getTurma();
        Integer limite = turma != null ? turma.getLimiteFaltas() : null;
        Integer faltas = insc.getNumeroFaltas();

        Integer faltasRestantes = null;
        if (limite != null && faltas != null)
            faltasRestantes = Math.max(0, limite - faltas);

        boolean aprovadoFaltas = false;
        if (limite == null || faltas == null) {
            aprovadoFaltas = true; // Não dá pra reprovar se não há informação
        } else {
            aprovadoFaltas = faltas <= limite;
        }

        return ResultadoAprovacaoDTO.builder()
                .inscricaoId(insc.getId())
                .alunoId(insc.getAluno() != null ? insc.getAluno().getMatricula() : null)
                .turmaId(insc.getTurma() != null ? insc.getTurma().getId() : null)

                // notas
                .ap1(insc.getAp1())
                .ap2(insc.getAp2())
                .ac(insc.getAc())
                .as(insc.getAs())  // OU getNotaAs()

                // faltas
                .numeroFaltas(faltas)
                .limiteFaltas(limite)
                .faltasRestantes(faltasRestantes)

                // cálculo do strategy
                .notaFinal(r.notaFinal())
                .usouAS(r.usouAS())

                // aprovação
                .aprovadoPorNota(r.aprovadoPorNota())
                .aprovadoPorFaltas(aprovadoFaltas)
                .aprovadoGeral(r.aprovadoPorNota() && aprovadoFaltas)

                .build();
    }
}
