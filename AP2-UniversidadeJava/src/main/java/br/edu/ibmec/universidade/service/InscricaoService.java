package br.edu.ibmec.universidade.service;

import br.edu.ibmec.universidade.dto.InscricaoDTO;
import br.edu.ibmec.universidade.entity.Aluno;
import br.edu.ibmec.universidade.entity.Inscricao;
import br.edu.ibmec.universidade.entity.Turma;
import br.edu.ibmec.universidade.repository.AlunoRepository;
import br.edu.ibmec.universidade.repository.InscricaoRepository;
import br.edu.ibmec.universidade.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class InscricaoService {
    private final InscricaoRepository repo;
    private final AlunoRepository alunoRepo;
    private final TurmaRepository turmaRepo;

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
        existing.setAno(dto.getAno());
        existing.setSemestre(dto.getSemestre());
        return toDTO(repo.save(existing));
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

    private InscricaoDTO toDTO(Inscricao i) {
        Integer alunoId = i.getAluno() == null ? null : i.getAluno().getMatricula();
        Integer turmaId    = i.getTurma() == null ? null : i.getTurma().getId();
        return InscricaoDTO.builder()
                .id(i.getId()).alunoId(alunoId).turmaId(turmaId)
                .ano(i.getAno()).semestre(i.getSemestre())
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
        i.setAno(dto.getAno());
        i.setSemestre(dto.getSemestre());
        return i;
    }
}
