package br.edu.ibmec.universidade.repository;

import br.edu.ibmec.universidade.entity.Inscricao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InscricaoRepository extends JpaRepository<Inscricao, Integer> {
    boolean existsByAlunoMatricula(int matricula);
    long countByAlunoMatricula(int matricula);
    List<Inscricao> findByAlunoMatricula(Integer matricula);
}