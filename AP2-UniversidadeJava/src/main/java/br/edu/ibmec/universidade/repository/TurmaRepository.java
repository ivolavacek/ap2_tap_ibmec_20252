package br.edu.ibmec.universidade.repository;

import br.edu.ibmec.universidade.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turma, Integer> {
}