package br.edu.ibmec.universidade.repository;

import br.edu.ibmec.universidade.entity.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer> {
}