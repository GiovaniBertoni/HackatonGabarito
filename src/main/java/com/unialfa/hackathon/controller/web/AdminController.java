package com.unialfa.hackathon.controller.web;

import com.unialfa.hackathon.model.entity.Aluno;
import com.unialfa.hackathon.model.entity.Disciplina;
import com.unialfa.hackathon.model.entity.Turma;
import com.unialfa.hackathon.model.entity.Usuario;
import com.unialfa.hackathon.model.entity.Perfil;
import com.unialfa.hackathon.repository.AlunoRepository;
import com.unialfa.hackathon.repository.DisciplinaRepository;
import com.unialfa.hackathon.repository.TurmaRepository;
import com.unialfa.hackathon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private TurmaRepository turmaRepository;
    @Autowired private DisciplinaRepository disciplinaRepository;
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping
    public String adminPanel() {
        return "admin/index";
    }

    @GetMapping("/turmas")
    public String listarTurmas(Model model) {
        model.addAttribute("turmas", turmaRepository.findAll());
        model.addAttribute("turma", new Turma());
        return "admin/turmas";
    }

    @PostMapping("/turmas")
    public String salvarTurma(@ModelAttribute Turma turma, RedirectAttributes redirectAttributes) {
        turmaRepository.save(turma);
        redirectAttributes.addFlashAttribute("sucesso", "Turma salva com sucesso!");
        return "redirect:/admin/turmas";
    }

    @GetMapping("/disciplinas")
    public String listarDisciplinas(Model model) {
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        model.addAttribute("disciplina", new Disciplina());
        return "admin/disciplinas";
    }

    @PostMapping("/disciplinas")
    public String salvarDisciplina(@ModelAttribute Disciplina disciplina, RedirectAttributes redirectAttributes) {
        disciplinaRepository.save(disciplina);
        redirectAttributes.addFlashAttribute("sucesso", "Disciplina salva com sucesso!");
        return "redirect:/admin/disciplinas";
    }

    @GetMapping("/alunos")
    public String listarAlunos(Model model) {
        model.addAttribute("alunos", alunoRepository.findAll());
        model.addAttribute("aluno", new Aluno());
        model.addAttribute("turmas", turmaRepository.findAll());
        return "admin/alunos";
    }

    @PostMapping("/alunos")
    @Transactional // Garante que todas as operações com o banco sejam feitas em uma única transação
    public String salvarAluno(@ModelAttribute Aluno aluno, @RequestParam String nomeUsuario, @RequestParam String emailUsuario, @RequestParam Long turmaId, RedirectAttributes redirectAttributes) {
        // Verifica se o email do usuário já existe
        if (usuarioRepository.findByEmail(emailUsuario).isPresent()) {
            redirectAttributes.addFlashAttribute("erro", "O e-mail '" + emailUsuario + "' já está em uso.");
            return "redirect:/admin/alunos";
        }

        // 1. Cria e salva o Usuario associado
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nomeUsuario);
        novoUsuario.setEmail(emailUsuario);
        novoUsuario.setSenha(passwordEncoder.encode("aluno123")); // Senha padrão
        novoUsuario.setPerfil(Perfil.ALUNO);
        usuarioRepository.save(novoUsuario);

        // 2. Associa o novo usuário ao aluno e salva o aluno
        aluno.setUsuario(novoUsuario);
        alunoRepository.save(aluno);

        // 3. Associa o aluno à turma
        Turma turma = turmaRepository.findById(turmaId).orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        turma.getAlunos().add(aluno); // Adiciona o novo aluno à lista de alunos da turma
        turmaRepository.save(turma); // Salva a turma com a lista de alunos atualizada

        redirectAttributes.addFlashAttribute("sucesso", "Aluno salvo com sucesso!");
        return "redirect:/admin/alunos";
    }
}
