package br.edu.ibmec.universidade.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "curso", "turmas" })
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @ManyToOne(optional = false)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(precision = 10, scale = 2)
    private BigDecimal mensalidadeBase;

    @OneToMany(mappedBy = "disciplina")
    private List<Turma> turmas = new ArrayList<>();

    public void addTurma(Turma turma) {
        if (turma == null) return;
        if (!this.turmas.contains(turma)) {
            this.turmas.add(turma);
        }
        if (turma.getDisciplina() != this) {
            turma.setDisciplina(this);
        }
    }

    public void removeTurma(Turma turma) {
        if (turma == null) return;
        this.turmas.remove(turma);
        if (turma.getDisciplina() == this) {
            turma.setDisciplina(null);
        }
    }

    public Disciplina(String nome, Curso curso, BigDecimal mensalidadeBase) {
        this.nome = nome;
        this.curso = curso;
        this.mensalidadeBase = mensalidadeBase;
    }
}
