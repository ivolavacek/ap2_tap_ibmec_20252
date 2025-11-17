package br.edu.ibmec.universidade.service;

import br.edu.ibmec.universidade.dto.AlunoDTO;
import br.edu.ibmec.universidade.dto.AlunoDetalheDTO;
import br.edu.ibmec.universidade.dto.AlunoResumoDTO;
import br.edu.ibmec.universidade.dto.DataNascimentoDTO;
import br.edu.ibmec.universidade.dto.InscricaoAlunoDTO;
import br.edu.ibmec.universidade.entity.Aluno;
import br.edu.ibmec.universidade.entity.Curso;
import br.edu.ibmec.universidade.entity.DataNascimento;
import br.edu.ibmec.universidade.repository.AlunoRepository;
import br.edu.ibmec.universidade.repository.CursoRepository;
import br.edu.ibmec.universidade.repository.InscricaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository repo;
    private final InscricaoRepository inscricaoRepo;
    private final CursoRepository cursoRepo;

    public List<AlunoResumoDTO> list() {
        return repo.findAll().stream().map(this::toResumoDTO).collect(Collectors.toList());
    }

    public AlunoDTO get(Integer id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public AlunoDetalheDTO getDetalhe(Integer id) {
        return repo.findById(id)
                .map(this::toDetalheDTO)
                .orElse(null);
    }

    public AlunoDTO create(AlunoDTO dto) {
        validarBolsaPercentual(dto.getBolsaPercentual());
        Aluno a = fromDTO(dto);
        return toDTO(repo.save(a));
    }

    public AlunoDTO update(Integer id, AlunoDTO dto) {
        Aluno existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        existing.setNome(dto.getNome());

        if (dto.getDataNascimento() != null) {
            DataNascimento dn =new DataNascimento(
                dto.getDataNascimento().getDia(),
                dto.getDataNascimento().getMes(),
                dto.getDataNascimento().getAno()
            );
            existing.setDataNascimento(dn);
            Integer idadeCalc = calcularIdade(dn);
            existing.setIdade(idadeCalc == null ? 0 : idadeCalc);
        } else {
            existing.setDataNascimento(null);
            existing.setIdade(0);
        }
        
        existing.setMatriculaAtiva(dto.isMatriculaAtiva());
        existing.setEstadoCivil(dto.getEstadoCivil());
        existing.setTelefones(dto.getTelefones());

        if (dto.getCursoId() != null) {
            Curso curso = cursoRepo.findById(dto.getCursoId())
                    .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
            existing.setCurso(curso);
        } else {
            existing.setCurso(null);
        }

        validarBolsaPercentual(dto.getBolsaPercentual());
        existing.setBolsaPercentual(dto.getBolsaPercentual());

        return toDTO(repo.save(existing));
    }

    public AlunoDTO atualizarCurso(Integer matricula, Integer cursoId) {
        Aluno aluno = repo.findById(matricula)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        aluno.setCurso(curso);
        return toDTO(repo.save(aluno));
    }

    public AlunoDTO atualizarBolsaPercentual(Integer matricula, BigDecimal novaBolsa) {
        validarBolsaPercentual(novaBolsa);

        Aluno aluno = repo.findById(matricula)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        aluno.setBolsaPercentual(novaBolsa);
        aluno = repo.save(aluno);

        return toDTO(aluno); // assume que você já tem esse método
    }

    private void validarBolsaPercentual(BigDecimal bolsa) {
        if (bolsa == null) {
            throw new IllegalArgumentException("Bolsa percentual não pode ser nula.");
        }
        if (bolsa.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Bolsa percentual não pode ser menor que 0.");
        }
        if (bolsa.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Bolsa percentual não pode ser maior que 100.");
        }
    }

    private Integer calcularIdade(DataNascimento dn) {
        if (dn == null) return null;
        try {
            LocalDate nascimento = LocalDate.of(dn.getAno(), dn.getMes(), dn.getDia());
            return Period.between(nascimento, LocalDate.now()).getYears();
        } catch (Exception e) {
            return null; // se der algum problema com data inválida
        }
    }

    public void delete(Integer id) {
        Aluno aluno = repo.findById(id).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (inscricaoRepo.existsByAlunoMatricula(aluno.getMatricula())) {
            long count = inscricaoRepo.countByAlunoMatricula(aluno.getMatricula());
            throw new RuntimeException("Não é possível deletar o aluno. Existem " + count + " inscrições associadas.");
        }
        repo.delete(aluno);
    }

    /* ===== Mapping ===== */

    private AlunoDTO toDTO(Aluno a) {
        DataNascimento dn = a.getDataNascimento();
        DataNascimentoDTO dndto = dn == null ? null : new DataNascimentoDTO(dn.getDia(), dn.getMes(), dn.getAno());
        Integer idadeCalc = calcularIdade(dn);
        Integer cursoId = (a.getCurso() == null) ? null : a.getCurso().getCodigo();

        return AlunoDTO.builder()
                .matricula(a.getMatricula())
                .nome(a.getNome())
                .dataNascimento(dndto)
                .idade(idadeCalc == null ? 0 : idadeCalc)
                .matriculaAtiva(a.isMatriculaAtiva())
                .estadoCivil(a.getEstadoCivil())
                .telefones(a.getTelefones())
                .cursoId(cursoId)
                .bolsaPercentual(a.getBolsaPercentual())
                .build();
    }

    private Aluno fromDTO(AlunoDTO dto) {
        DataNascimento dn = dto.getDataNascimento() == null ? null :
                new DataNascimento(dto.getDataNascimento().getDia(), dto.getDataNascimento().getMes(), dto.getDataNascimento().getAno());
        Aluno a = new Aluno();
        a.setMatricula(dto.getMatricula());
        a.setNome(dto.getNome());
        a.setDataNascimento(dn);

        Integer idadeCalc = calcularIdade(dn);
        a.setIdade(idadeCalc == null ? 0 : idadeCalc);

        a.setMatriculaAtiva(dto.isMatriculaAtiva());
        a.setEstadoCivil(dto.getEstadoCivil());
        a.setTelefones(dto.getTelefones());

        if (dto.getCursoId() != null) {
            Curso curso = cursoRepo.findById(dto.getCursoId())
                    .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
            a.setCurso(curso);
        } else {
            a.setCurso(null);
        }
        a.setBolsaPercentual(dto.getBolsaPercentual() == null ? BigDecimal.ZERO : dto.getBolsaPercentual());
        
        return a;
    }

    private AlunoResumoDTO toResumoDTO(Aluno a) {
        Integer cursoId = (a.getCurso() == null) ? null : a.getCurso().getCodigo();
        String nomeCurso = (a.getCurso() == null) ? null : a.getCurso().getNome();

        return AlunoResumoDTO.builder()
                .matricula(a.getMatricula())
                .nome(a.getNome())
                .cursoId(cursoId)
                .nomeCurso(nomeCurso)
                .build();
    }

    private AlunoDetalheDTO toDetalheDTO(Aluno a) {
        DataNascimento dn = a.getDataNascimento();
        DataNascimentoDTO dndto = dn == null ? null :
                new DataNascimentoDTO(dn.getDia(), dn.getMes(), dn.getAno());
        Integer idadeCalc = calcularIdade(dn);

        Integer cursoId = (a.getCurso() == null) ? null : a.getCurso().getCodigo();
        String cursoNome = (a.getCurso() == null) ? null : a.getCurso().getNome();

        // Montar lista de inscrições com turma, disciplina e curso
        List<InscricaoAlunoDTO> inscricoesDTO = a.getInscricoes().stream()
            .map(inscricao -> {
                var turma = inscricao.getTurma();
                var disciplina = (turma == null) ? null : turma.getDisciplina();
                var cursoDisciplina = (disciplina == null) ? null : disciplina.getCurso();

                Integer cursoIdDisc = (cursoDisciplina == null) ? cursoId : cursoDisciplina.getCodigo();
                String cursoNomeDisc = (cursoDisciplina == null) ? cursoNome : cursoDisciplina.getNome();

                return InscricaoAlunoDTO.builder()
                        .id(inscricao.getId()) // ajuste o tipo se o ID não for Long
                        .turmaId(turma == null ? null : turma.getId())
                        .turmaAtiva(turma != null && turma.isAtiva())
                        .semestre(turma == null ? null : turma.getSemestre())
                        .ano(turma == null ? null : turma.getAno())
                        .disciplinaId(disciplina == null ? null : disciplina.getId())
                        .disciplinaNome(disciplina == null ? null : disciplina.getNome())
                        .cursoId(cursoIdDisc)
                        .cursoNome(cursoNomeDisc)
                        .build();
            })
            .collect(Collectors.toList());

        return AlunoDetalheDTO.builder()
                .matricula(a.getMatricula())
                .nome(a.getNome())
                .dataNascimento(dndto)
                .idade(idadeCalc == null ? 0 : idadeCalc)
                .matriculaAtiva(a.isMatriculaAtiva())
                .estadoCivil(a.getEstadoCivil())
                .telefones(a.getTelefones())
                .cursoId(cursoId)
                .cursoNome(cursoNome)
                .bolsaPercentual(a.getBolsaPercentual())
                .inscricoes(inscricoesDTO)
                .build();
    }

}