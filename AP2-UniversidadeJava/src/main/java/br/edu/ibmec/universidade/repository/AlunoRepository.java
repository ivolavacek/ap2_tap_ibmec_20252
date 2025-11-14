package br.edu.ibmec.universidade.repository;

import br.edu.ibmec.universidade.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
}