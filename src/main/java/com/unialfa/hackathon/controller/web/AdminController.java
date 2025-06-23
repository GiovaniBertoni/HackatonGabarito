package com.unialfa.hackathon.controller.web;

import com.unialfa.hackathon.model.entity.Aluno;
import com.unialfa.hackathon.model.entity.Disciplina;
import com.unialfa.hackathon.model.entity.Turma;
import com.unialfa.hackathon.model.entity.Usuario;
import com.unialfa.hackathon.model.entity.Perfil;
import com.unialfa.hackathon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
    @Autowired private RespostaAlunoRepository respostaAlunoRepository;

    // <<< Injetado o repositório de provas para verificação >>>
    @Autowired private ProvaRepository provaRepository;

    @GetMapping
    public String adminPanel() {
        return "admin/index";
    }

    // --- GESTÃO DE TURMAS ---

    @GetMapping("/turmas")
    public String listarTurmas(Model model) {
        model.addAttribute("turmas", turmaRepository.findAll());
        model.addAttribute("turma", new Turma());
        return "admin/turmas";
    }

    @PostMapping("/turmas")
    public String salvarTurma(@ModelAttribute Turma turma, RedirectAttributes redirectAttributes) {
        boolean turmaExistente = turmaRepository.existsByNomeAndAnoLetivo(turma.getNome(), turma.getAnoLetivo());

        if (turmaExistente) {
            redirectAttributes.addFlashAttribute("erro", "Já existe uma turma com esse nome.");
            return "redirect:/admin/turmas";
        }

        turmaRepository.save(turma);
        redirectAttributes.addFlashAttribute("sucesso", "Turma Cadastrada com sucesso!");
        return "redirect:/admin/turmas";
    }

    @GetMapping("/turmas/editar/{id}")
    public String editarTurmaForm(@PathVariable("id") Long id, Model model) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Turma inválida Id:" + id));
        model.addAttribute("turma", turma);
        return "admin/editar-turma";
    }

    @PostMapping("/turmas/atualizar/{id}")
    public String atualizarTurma(@PathVariable("id") Long id, @ModelAttribute Turma turma, RedirectAttributes redirectAttributes) {
        Turma turmaExistente = turmaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Turma inválida Id:" + id));
        turmaExistente.setNome(turma.getNome());
        turmaExistente.setAno(turma.getAno());
        turmaRepository.save(turmaExistente);
        redirectAttributes.addFlashAttribute("sucesso", "Turma atualizada com sucesso!");
        return "redirect:/admin/turmas";
    }


    // <<< Eliminar Turma >>>
    @GetMapping("/turmas/eliminar/{id}")
    public String eliminarTurma(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        // Verifica se a turma está a ser usada em alguma prova
        if (!provaRepository.findByTurmaId(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Não pode eliminar uma turma que já possui provas associadas.");
            return "redirect:/admin/turmas";
        }
        turmaRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("sucesso", "Turma eliminada com sucesso!");
        return "redirect:/admin/turmas";
    }


    // --- GESTÃO DE DISCIPLINAS ---
    @GetMapping("/disciplinas")
    public String listarDisciplinas(Model model) {
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        model.addAttribute("disciplina", new Disciplina());
        return "admin/disciplinas";
    }

    @PostMapping("/disciplinas")
    public String salvarDisciplina(@ModelAttribute Disciplina disciplina, RedirectAttributes redirectAttributes) {
        if (disciplinaRepository.existsByNome(disciplina.getNome())) {
            redirectAttributes.addFlashAttribute("erro", "Já existe uma disciplina com esse nome.");
            return "redirect:/admin/disciplinas";
        }

        disciplinaRepository.save(disciplina);
        redirectAttributes.addFlashAttribute("sucesso", "Disciplina cadastrada com sucesso!");
        return "redirect:/admin/disciplinas";
    }

    @GetMapping("/disciplinas/editar/{id}")
    public String editarDisciplinaForm(@PathVariable("id") Long id, Model model) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disciplina inválida Id:" + id));
        model.addAttribute("disciplina", disciplina);
        return "admin/editar-disciplina";
    }

    @PostMapping("/disciplinas/atualizar/{id}")
    public String atualizarDisciplina(@PathVariable("id") Long id, @ModelAttribute Disciplina disciplina, RedirectAttributes redirectAttributes) {
        Disciplina disciplinaExistente = disciplinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disciplina inválida Id:" + id));
        disciplinaExistente.setNome(disciplina.getNome());
        disciplinaRepository.save(disciplinaExistente);
        redirectAttributes.addFlashAttribute("sucesso", "Disciplina atualizada com sucesso!");
        return "redirect:/admin/disciplinas";
    }

    // <<< Eliminar Disciplina >>>
    @GetMapping("/disciplinas/eliminar/{id}")
    public String eliminarDisciplina(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        // Verifica se a disciplina está a ser usada em alguma prova
        if (!provaRepository.findByDisciplinaId(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Não pode eliminar uma disciplina que já possui provas associadas.");
            return "redirect:/admin/disciplinas";
        }
        disciplinaRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("sucesso", "Disciplina eliminada com sucesso!");
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
    @Transactional
    public String salvarAluno(@ModelAttribute Aluno aluno,
                              @RequestParam String nomeUsuario,
                              @RequestParam String emailUsuario,
                              @RequestParam String senhaUsuario,
                              @RequestParam Long turmaId,
                              RedirectAttributes redirectAttributes) {
        if (usuarioRepository.findByEmail(emailUsuario).isPresent()) {
            redirectAttributes.addFlashAttribute("erro", "O e-mail '" + emailUsuario + "' já está em uso.");
            return "redirect:/admin/alunos";
        }
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nomeUsuario);
        novoUsuario.setEmail(emailUsuario);
        novoUsuario.setSenha(passwordEncoder.encode(senhaUsuario));
        novoUsuario.setPerfil(Perfil.ALUNO);
        usuarioRepository.save(novoUsuario);

        aluno.setUsuario(novoUsuario);
        alunoRepository.save(aluno);  // Salva para gerar ID

        // Agora gera o RA com o ID gerado
        String raGerado = Aluno.gerarRa(aluno.getId());
        aluno.setRa(raGerado);
        alunoRepository.save(aluno);  // Atualiza o RA

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        turma.getAlunos().add(aluno);
        turmaRepository.save(turma);

        redirectAttributes.addFlashAttribute("sucesso", "Aluno Cadastrado com sucesso!");
        return "redirect:/admin/alunos";
    }


    @GetMapping("/alunos/editar/{id}")
    public String editarAlunoForm(@PathVariable("id") Long id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Aluno inválido Id:" + id));
        model.addAttribute("aluno", aluno);
        return "admin/editar-aluno";
    }

    @PostMapping("/alunos/atualizar/{id}")
    @Transactional
    public String atualizarAluno(@PathVariable("id") Long id, @ModelAttribute Aluno aluno, @RequestParam String novaSenha, RedirectAttributes redirectAttributes) {
        Aluno alunoExistente = alunoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Aluno inválido Id:" + id));
        alunoExistente.setRa(aluno.getRa());
        Usuario usuario = alunoExistente.getUsuario();
        usuario.setNome(aluno.getUsuario().getNome());
        usuario.setEmail(aluno.getUsuario().getEmail());
        if (StringUtils.hasText(novaSenha)) {
            usuario.setSenha(passwordEncoder.encode(novaSenha));
        }
        alunoRepository.save(alunoExistente);
        redirectAttributes.addFlashAttribute("sucesso", "Aluno atualizado com sucesso!");
        return "redirect:/admin/alunos";
    }

    @GetMapping("/alunos/eliminar/{id}")
    @Transactional
    public String eliminarAluno(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno inválido Id:" + id));

        boolean temVinculos = respostaAlunoRepository.existsByAlunoId(aluno.getId());

        if (temVinculos) {
            redirectAttributes.addFlashAttribute("erro", "Não é possível eliminar o aluno. Há vínculos com provas realizadas.");
            return "redirect:/admin/alunos";
        }

        for (Turma turma : aluno.getTurmas()) {
            turma.getAlunos().remove(aluno);
        }

        alunoRepository.delete(aluno);
        redirectAttributes.addFlashAttribute("sucesso", "Aluno eliminado com sucesso!");
        return "redirect:/admin/alunos";
    }

}