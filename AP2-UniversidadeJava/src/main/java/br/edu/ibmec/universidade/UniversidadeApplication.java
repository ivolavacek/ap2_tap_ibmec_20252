package br.edu.ibmec.universidade;

import br.edu.ibmec.universidade.entity.*;
import br.edu.ibmec.universidade.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class UniversidadeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversidadeApplication.class, args);
    }

    // Cria e insere dados ao iniciar o app
    @Bean
    CommandLineRunner initDatabase(
            CursoRepository cursoRepo,
            DisciplinaRepository discRepo,
            ProfessorRepository profRepo,
            TurmaRepository turmaRepo,
            AlunoRepository alunoRepo,
            InscricaoRepository inscRepo
    ) {
        return args -> {
            if (cursoRepo.count() > 0) return; // evita duplicar

            // ----- Cursos -----
            Curso cdia  = new Curso("Ciência de Dados e IA");
            Curso egi = new Curso("Engenharia e Gestão Industrial");
            cursoRepo.saveAll(List.of(cdia, egi));

            // ----- Disciplinas -----
            Disciplina bd  = new Disciplina("Banco de Dados", cdia, new BigDecimal("500.00"));
            Disciplina ml  = new Disciplina("Machine Learning", cdia, new BigDecimal("500.00"));
            Disciplina log = new Disciplina("Logística", egi, new BigDecimal("500.00"));
            discRepo.saveAll(List.of(bd, ml, log));

            // ----- Professores -----
            Professor talita  = new Professor("Talita");
            Professor thiago = new Professor("Thiago");
            Professor luis = new Professor("Luís");
            profRepo.saveAll(List.of(talita, thiago, luis));

            // ----- Turmas -----
            Turma turmaBd = new Turma(bd, talita);
            Turma turmaMl  = new Turma(ml, thiago);
            Turma turmaLog  = new Turma(log, luis);
            turmaRepo.saveAll(List.of(turmaBd, turmaMl, turmaLog));

            // ----- Alunos -----
            DataNascimento dnIvo   = new DataNascimento(7, 3, 2000);
            DataNascimento dnDan = new DataNascimento(11, 5, 1999);

            Aluno ivo = new Aluno(
                    100, "Ivo Lavacek", dnIvo, 24, true,
                    EstadoCivil.solteiro, new ArrayList<>(List.of("21999990000")),
                    cdia, new BigDecimal("10.00"), new ArrayList<>()
            );
            Aluno dan = new Aluno(
                    101, "Dan Heinz", dnDan, 25, true,
                    EstadoCivil.solteiro, new ArrayList<>(List.of("21988880000")),
                    egi, new BigDecimal("15.00"), new ArrayList<>()
            );

            alunoRepo.saveAll(List.of(ivo, dan));

            // ----- Inscrições -----
            Inscricao insc1 = new Inscricao(ivo, turmaBd, 2025, 2, true);
            Inscricao insc2 = new Inscricao(ivo, turmaMl,  2025, 2, true);
            Inscricao insc3 = new Inscricao(dan, turmaLog, 2025, 2, true);
            inscRepo.saveAll(List.of(insc1, insc2, insc3));

            System.out.println("[OK] Dados iniciais carregados com sucesso!");
        };
    }
}
