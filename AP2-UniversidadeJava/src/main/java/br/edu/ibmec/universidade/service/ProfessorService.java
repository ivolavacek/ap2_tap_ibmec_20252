package br.edu.ibmec.universidade.service;

import br.edu.ibmec.universidade.dto.ProfessorDTO;
import br.edu.ibmec.universidade.entity.Professor;
import br.edu.ibmec.universidade.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository repo;

    public List<ProfessorDTO> list() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProfessorDTO get(Integer id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public ProfessorDTO create(ProfessorDTO dto) {
        Professor p = fromDTO(dto);
        return toDTO(repo.save(p));
    }

    public ProfessorDTO update(Integer id, ProfessorDTO dto) {
        Professor existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        existing.setNome(dto.getNome());
        return toDTO(repo.save(existing));
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

    private ProfessorDTO toDTO(Professor p) {
        return ProfessorDTO.builder().id(p.getId()).nome(p.getNome()).build();
    }

    private Professor fromDTO(ProfessorDTO dto) {
        Professor p = new Professor();
        // p.setId(dto.getId());
        p.setNome(dto.getNome());
        return p;
    }
}
