package br.edu.ibmec.universidade.service;

import br.edu.ibmec.universidade.dto.AlunoDTO;
import br.edu.ibmec.universidade.dto.DataNascimentoDTO;
import br.edu.ibmec.universidade.entity.Aluno;
import br.edu.ibmec.universidade.entity.Curso;
import br.edu.ibmec.universidade.entity.DataNascimento;
import br.edu.ibmec.universidade.repository.AlunoRepository;
import br.edu.ibmec.universidade.repository.CursoRepository;
import br.edu.ibmec.universidade.repository.InscricaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository repo;
    private final InscricaoRepository inscricaoRepo;
    private final CursoRepository cursoRepo;

    public List<AlunoDTO> list() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AlunoDTO get(Integer id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public AlunoDTO create(AlunoDTO dto) {
        Aluno a = fromDTO(dto);
        return toDTO(repo.save(a));
    }

    public AlunoDTO update(Integer id, AlunoDTO dto) {
        Aluno existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        // atualiza campos
        existing.setNome(dto.getNome());
        if (dto.getDataNascimento() != null) {
            existing.setDataNascimento(new DataNascimento(
                dto.getDataNascimento().getDia(),
                dto.getDataNascimento().getMes(),
                dto.getDataNascimento().getAno()
            ));
        } else {
            existing.setDataNascimento(null);
        }
        existing.setIdade(dto.getIdade() == null ? 0 : dto.getIdade());
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

        return toDTO(repo.save(existing));
    }

    public AlunoDTO vincularCurso(Integer matricula, Integer cursoId) {
        Aluno aluno = repo.findById(matricula)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        aluno.setCurso(curso);
        return toDTO(repo.save(aluno));
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
        Integer cursoId = (a.getCurso() == null) ? null : a.getCurso().getCodigo();

        return AlunoDTO.builder()
                .matricula(a.getMatricula())
                .nome(a.getNome())
                .dataNascimento(dndto)
                .idade(a.getIdade())
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
        a.setIdade(dto.getIdade() == null ? 0 : dto.getIdade());
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
}
