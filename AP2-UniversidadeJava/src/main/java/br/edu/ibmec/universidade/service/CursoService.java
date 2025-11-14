package br.edu.ibmec.universidade.service;

import br.edu.ibmec.universidade.dto.CursoDTO;
import br.edu.ibmec.universidade.entity.Curso;
import br.edu.ibmec.universidade.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class CursoService {
    private final CursoRepository repo;

    public List<CursoDTO> list() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CursoDTO get(Integer id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public CursoDTO create(CursoDTO dto) {
        Curso c = fromDTO(dto);
        return toDTO(repo.save(c));
    }

    public CursoDTO update(Integer id, CursoDTO dto) {
        Curso existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        existing.setNome(dto.getNome());
        return toDTO(repo.save(existing));
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

    private CursoDTO toDTO(Curso c) {
        return CursoDTO.builder().codigo(c.getCodigo()).nome(c.getNome()).build();
    }

    private Curso fromDTO(CursoDTO dto) {
        Curso c = new Curso();
        // c.setCodigo(dto.getCodigo());
        c.setNome(dto.getNome());
        return c;
    }
}
