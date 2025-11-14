package br.edu.ibmec.universidade.repository;

import br.edu.ibmec.universidade.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
}