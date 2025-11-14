package br.edu.ibmec.universidade.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "turmas")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @OneToMany(mappedBy = "professor")
    private List<Turma> turmas = new ArrayList<>();

    public void addTurma(Turma turma) {
        if (turma == null) return;
        if (!this.turmas.contains(turma)) {
            this.turmas.add(turma);
        }
        if (turma.getProfessor() != this) {
            turma.setProfessor(this);
        }
    }

    public void removeTurma(Turma turma) {
        if (turma == null) return;
        this.turmas.remove(turma);
        if (turma.getProfessor() == this) {
            turma.setProfessor(null);
        }
    }

    public Professor(String nome) {
        this.nome = nome;
    }
}
