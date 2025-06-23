package com.unialfa.hackathon.config;

import com.unialfa.hackathon.model.entity.*;
import com.unialfa.hackathon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private TurmaRepository turmaRepository;
    @Autowired private DisciplinaRepository disciplinaRepository;
    @Autowired private ProvaRepository provaRepository;
    @Autowired private GabaritoRepository gabaritoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println(">>> INICIANDO A CRIAÇÃO DE DADOS DE TESTE VIA JAVA <<<");

        if (usuarioRepository.count() == 0) {
            System.out.println("Banco de dados vazio. Populando com dados iniciais...");

            // 1. Criar Usuários
            Usuario admin = new Usuario();
            admin.setNome("Admin Geral");
            admin.setEmail("admin@unialfa.com");
            admin.setSenha(passwordEncoder.encode("senha123"));
            admin.setPerfil(Perfil.ADMIN);
            usuarioRepository.save(admin);

            Usuario professor = new Usuario();
            professor.setNome("Prof. Severino");
            professor.setEmail("professor@unialfa.com");
            professor.setSenha(passwordEncoder.encode("senha123"));
            professor.setPerfil(Perfil.PROFESSOR);
            usuarioRepository.save(professor);

            Usuario usuarioAluno1 = new Usuario();
            usuarioAluno1.setNome("Godofredo Arantes");
            usuarioAluno1.setEmail("aluno.godofredo@unialfa.com");
            usuarioAluno1.setSenha(passwordEncoder.encode("senha123"));
            usuarioAluno1.setPerfil(Perfil.ALUNO);
            usuarioRepository.save(usuarioAluno1);

            // 2. Criar Alunos e associar
            Aluno aluno1 = new Aluno();
            aluno1.setRa("111222");
            aluno1.setUsuario(usuarioAluno1);
            alunoRepository.save(aluno1);

            // 3. Criar Disciplinas e Turmas
            Disciplina disciplinaWeb = new Disciplina();
            disciplinaWeb.setNome("Frameworks de Desenvolvimento Web");
            disciplinaRepository.save(disciplinaWeb);

            Turma turmaSI = new Turma();
            turmaSI.setNome("Sistemas para Internet - 5º Período");
            turmaSI.setAno(2025);
            turmaRepository.save(turmaSI);

            // 4. Associar Alunos a Turmas
            turmaSI.getAlunos().add(aluno1);
            turmaRepository.save(turmaSI);

            // 5. Criar Prova e Gabarito
            Prova prova = new Prova();
            prova.setTitulo("Avaliação 1 - Spring Boot");
            prova.setDataAplicacao(new Date());
            prova.setProfessor(professor);
            prova.setTurma(turmaSI);
            prova.setDisciplina(disciplinaWeb);
            Prova provaSalva = provaRepository.save(prova);

            Gabarito gabarito = new Gabarito();
            gabarito.setProva(provaSalva);

            // CORREÇÃO:
            Map<Integer, GabaritoQuestao> respostasComValor = Map.of(
                    1, new GabaritoQuestao("A", 2.0),
                    2, new GabaritoQuestao("C", 2.0),
                    3, new GabaritoQuestao("D", 2.0),
                    4, new GabaritoQuestao("B", 2.0),
                    5, new GabaritoQuestao("E", 2.0)
            );
            gabarito.setRespostas(respostasComValor);
            gabaritoRepository.save(gabarito);

            System.out.println(">>> DADOS DE TESTE CRIADOS COM SUCESSO! <<<");
        } else {
            System.out.println(">>> O banco de dados já contém dados. A inicialização foi pulada. <<<");
        }
    }
}
