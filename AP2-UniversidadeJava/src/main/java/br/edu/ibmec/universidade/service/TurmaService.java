package br.edu.ibmec.universidade.service;

import br.edu.ibmec.universidade.dto.TurmaDTO;
import br.edu.ibmec.universidade.entity.Disciplina;
import br.edu.ibmec.universidade.entity.Professor;
import br.edu.ibmec.universidade.entity.Turma;
import br.edu.ibmec.universidade.repository.DisciplinaRepository;
import br.edu.ibmec.universidade.repository.ProfessorRepository;
import br.edu.ibmec.universidade.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class TurmaService {
    private final TurmaRepository repo;
    private final DisciplinaRepository disciplinaRepo;
    private final ProfessorRepository professorRepo;

    public List<TurmaDTO> list() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TurmaDTO get(Integer id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public TurmaDTO create(TurmaDTO dto) {
        Turma t = fromDTO(dto);
        validateSemestre(t.getSemestre());
        return toDTO(repo.save(t));
    }

    private void validateSemestre(Integer semestre) {
        if (semestre == null || (semestre != 1 && semestre != 2)) {
            throw new IllegalArgumentException("Semestre deve ser 1 ou 2");
        }
    }

    public TurmaDTO atualizarStatusTurma(Integer id, boolean ativa) {
        Turma turma = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        turma.setAtiva(ativa);
        turma = repo.save(turma);

        return toDTO(turma);
    }


    public TurmaDTO update(Integer id, TurmaDTO dto) {
        Turma existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        if (dto.getDisciplinaId() != null) {
            Disciplina d = disciplinaRepo.findById(dto.getDisciplinaId())
                    .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
            existing.setDisciplina(d);
        }
        if (dto.getProfessorId() != null) {
            Professor p = professorRepo.findById(dto.getProfessorId())
                    .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
            existing.setProfessor(p);
        }
        return toDTO(repo.save(existing));
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

    private TurmaDTO toDTO(Turma t) {
        Integer disciplinaId = t.getDisciplina() == null ? null : t.getDisciplina().getId();
        Integer professorId  = t.getProfessor()  == null ? null : t.getProfessor().getId();
        String disciplinaNome = t.getDisciplina() == null ? null : t.getDisciplina().getNome();
        String professorNome  = t.getProfessor() == null ? null : t.getProfessor().getNome();

        return TurmaDTO.builder()
                .id(t.getId())
                .disciplinaId(disciplinaId)
                .professorId(professorId)
                .semestre(t.getSemestre())   // <-- adicionado
                .ano(t.getAno())             // <-- adicionado
                .limiteFaltas(t.getLimiteFaltas()) // <-- adicionado
                .disciplinaNome(disciplinaNome)
                .professorNome(professorNome)
                .ativa(t.isAtiva())
                .build();
    }

    private Turma fromDTO(TurmaDTO dto) {
        Turma t = new Turma();
        // t.setId(dto.getId());
        if (dto.getDisciplinaId() != null) {
            Disciplina d = disciplinaRepo.findById(dto.getDisciplinaId())
                    .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
            t.setDisciplina(d);
        }
        if (dto.getProfessorId() != null) {
            Professor p = professorRepo.findById(dto.getProfessorId())
                    .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
            t.setProfessor(p);
        }
        t.setAno(dto.getAno());
        t.setSemestre(dto.getSemestre());
        t.setLimiteFaltas(dto.getLimiteFaltas());
        t.setAtiva(dto.getAtiva());
        return t;
    }
}
