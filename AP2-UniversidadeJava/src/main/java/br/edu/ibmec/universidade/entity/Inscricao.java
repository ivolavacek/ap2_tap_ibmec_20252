package br.edu.ibmec.universidade.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "aluno", "turma" })
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "turma_id")
    private Turma turma;

    private Integer ano;      // ex.: 2025
    private Integer semestre;  // ex.: "2025.1" ou "2025.2"
    private boolean ativa = true;

    public Inscricao(Aluno aluno, Turma turma, Integer ano, Integer semestre, boolean ativa) {
        this.aluno = aluno;
        this.turma = turma;
        this.ano = ano;
        this.semestre = semestre;
        this.ativa = ativa;
    }
}
