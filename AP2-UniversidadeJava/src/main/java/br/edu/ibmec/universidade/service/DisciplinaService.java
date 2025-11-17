package br.edu.ibmec.universidade.service;

import br.edu.ibmec.universidade.dto.DisciplinaDTO;
import br.edu.ibmec.universidade.entity.Curso;
import br.edu.ibmec.universidade.entity.Disciplina;
import br.edu.ibmec.universidade.repository.CursoRepository;
import br.edu.ibmec.universidade.repository.DisciplinaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class DisciplinaService {
    private final DisciplinaRepository repo;
    private final CursoRepository cursoRepo;

    public List<DisciplinaDTO> list() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public DisciplinaDTO get(Integer id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public DisciplinaDTO create(DisciplinaDTO dto) {
        Disciplina d = fromDTO(dto);
        return toDTO(repo.save(d));
    }

    public DisciplinaDTO update(Integer id, DisciplinaDTO dto) {
        Disciplina existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
        existing.setNome(dto.getNome());
        if (dto.getCursoId() != null) {
            Curso curso = cursoRepo.findById(dto.getCursoId())
                    .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
            existing.setCurso(curso);
        }
        return toDTO(repo.save(existing));
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

    private DisciplinaDTO toDTO(Disciplina d) {
        Integer cursoId = d.getCurso() == null ? null : d.getCurso().getCodigo();
        String cursoNome = d.getCurso() == null ? null : d.getCurso().getNome();
        return DisciplinaDTO.builder().id(d.getId()).nome(d.getNome()).cursoId(cursoId).cursoNome(cursoNome).build();
    }

    private Disciplina fromDTO(DisciplinaDTO dto) {
        Disciplina d = new Disciplina();
        // d.setId(dto.getId());
        d.setNome(dto.getNome());
        if (dto.getCursoId() != null) {
            Curso c = cursoRepo.findById(dto.getCursoId())
                    .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
            d.setCurso(c);
        } else {
            d.setCurso(null);
        }
        return d;
    }
}
