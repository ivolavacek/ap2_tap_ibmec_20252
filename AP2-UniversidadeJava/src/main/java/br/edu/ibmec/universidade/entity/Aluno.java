package br.edu.ibmec.universidade.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@ToString(exclude = "inscricoes")
public class Aluno {

    @Id
    private Integer matricula;

    private String nome;

    @Embedded
    private DataNascimento dataNascimento;

    private Integer idade;

    private boolean matriculaAtiva;

    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;

    @ElementCollection
    private List<String> telefones = new ArrayList<>();

    @ManyToOne(optional = true)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(precision = 5, scale = 2)
    private BigDecimal bolsaPercentual;

    @OneToMany(mappedBy = "aluno")
    private List<Inscricao> inscricoes = new ArrayList<>();

    // Helpers simples
    public void addInscricao(Inscricao inscricao) {
        if (inscricao == null) return;
        if (!this.inscricoes.contains(inscricao)) {
            this.inscricoes.add(inscricao);
        }
        if (inscricao.getAluno() != this) {
            inscricao.setAluno(this);
        }
    }

    public void removeInscricao(Inscricao inscricao) {
        if (inscricao == null) return;
        this.inscricoes.remove(inscricao);
        if (inscricao.getAluno() == this) {
            inscricao.setAluno(null);
        }
    }

    public Aluno(Integer matricula, String nome, DataNascimento dataNascimento, Integer idade, boolean matriculaAtiva,
                 EstadoCivil estadoCivil, List<String> telefones) {
        this.matricula = matricula;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.idade = idade;
        this.matriculaAtiva = matriculaAtiva;
        this.estadoCivil = estadoCivil;
        this.telefones = telefones;
    }
}
