package br.edu.ibmec.universidade.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
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
@ToString(exclude = "disciplinas")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    private String nome;

    @Column(precision = 10, scale = 2)
    private BigDecimal mensalidadeBase;

    @OneToMany(mappedBy = "curso")
    private List<Disciplina> disciplinas = new ArrayList<>();

    public void addDisciplina(Disciplina disciplina) {
        if (disciplina == null) return;
        if (!this.disciplinas.contains(disciplina)) {
            this.disciplinas.add(disciplina);
        }
        if (disciplina.getCurso() != this) {
            disciplina.setCurso(this);
        }
    }

    public void removeDisciplina(Disciplina disciplina) {
        if (disciplina == null) return;
        this.disciplinas.remove(disciplina);
        if (disciplina.getCurso() == this) {
            disciplina.setCurso(null);
        }
    }
    
    public Curso(String nome, BigDecimal mensalidadeBase) {
        this.nome = nome;
        this.mensalidadeBase = mensalidadeBase;
    }
}
