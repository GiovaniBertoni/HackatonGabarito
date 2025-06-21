package com.unialfa.hackathon.controller.web;

import com.unialfa.hackathon.model.entity.*;
import com.unialfa.hackathon.repository.AlunoRepository;
import com.unialfa.hackathon.repository.GabaritoRepository;
import com.unialfa.hackathon.repository.RespostaAlunoRepository;
import com.unialfa.hackathon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/aluno")
@PreAuthorize("hasRole('ALUNO')")
public class AlunoController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private RespostaAlunoRepository respostaAlunoRepository;
    @Autowired private GabaritoRepository gabaritoRepository;

    @GetMapping
    public String painelAluno(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
        Aluno aluno = alunoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado para este utilizador"));
        List<RespostaAluno> minhasRespostas = respostaAlunoRepository.findByAlunoId(aluno.getId());
        Map<Long, Double> notasMaximas = new HashMap<>();
        for (RespostaAluno resposta : minhasRespostas) {
            double notaMaxima = gabaritoRepository.findByProvaId(resposta.getProva().getId())
                    .map(gabarito -> gabarito.getRespostas().values().stream()
                            .mapToDouble(GabaritoQuestao::getValor)
                            .sum())
                    .orElse(0.0);
            notasMaximas.put(resposta.getProva().getId(), notaMaxima);
        }
        model.addAttribute("respostas", minhasRespostas);
        model.addAttribute("notasMaximas", notasMaximas);
        return "aluno/index";
    }

    // <<< Exibir a correção detalhada da prova >>>
    @GetMapping("/gabarito/{id}")
    public String verGabarito(@PathVariable("id") Long respostaId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // 1. Busca a resposta do aluno pelo ID
        RespostaAluno respostaAluno = respostaAlunoRepository.findById(respostaId)
                .orElseThrow(() -> new RuntimeException("Resposta não encontrada"));

        // 2. Garante que o aluno logado só pode ver as suas próprias notas
        Usuario usuarioLogado = usuarioRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        if (!respostaAluno.getAluno().getUsuario().getId().equals(usuarioLogado.getId())) {
            // Se o ID do dono da resposta for diferente do ID do utilizador logado, nega o acesso.
            return "redirect:/aluno?error=acesso_negado";
        }

        // 3. Busca o gabarito oficial da prova
        Gabarito gabaritoOficial = gabaritoRepository.findByProvaId(respostaAluno.getProva().getId())
                .orElseThrow(() -> new RuntimeException("Gabarito oficial não encontrado"));

        // 4. Envia os dados para a nova página HTML
        model.addAttribute("respostaAluno", respostaAluno);
        model.addAttribute("gabaritoOficial", gabaritoOficial);

        return "aluno/ver-gabarito"; // Aponta para um novo ficheiro HTML
    }
}
