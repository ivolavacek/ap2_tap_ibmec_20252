package br.edu.ibmec.universidade.repository;

import br.edu.ibmec.universidade.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
}