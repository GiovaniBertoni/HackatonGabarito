package com.unialfa.hackathon.controller.web;

import com.unialfa.hackathon.model.entity.Aluno;
import com.unialfa.hackathon.model.entity.GabaritoQuestao;
import com.unialfa.hackathon.model.entity.RespostaAluno;
import com.unialfa.hackathon.model.entity.Usuario;
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
    @Autowired private GabaritoRepository gabaritoRepository; // Para buscar a nota máxima

    @GetMapping
    public String painelAluno(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // 1. Encontra o utilizador logado
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        // 2. Encontra o aluno correspondente
        Aluno aluno = alunoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado para este utilizador"));

        // 3. Busca todas as respostas (notas) deste aluno
        List<RespostaAluno> minhasRespostas = respostaAlunoRepository.findByAlunoId(aluno.getId());

        // 4. Para cada resposta, calcula a nota máxima da respetiva prova
        Map<Long, Double> notasMaximas = new HashMap<>();
        for (RespostaAluno resposta : minhasRespostas) {
            double notaMaxima = gabaritoRepository.findByProvaId(resposta.getProva().getId())
                    .map(gabarito -> gabarito.getRespostas().values().stream()
                            .mapToDouble(GabaritoQuestao::getValor)
                            .sum())
                    .orElse(0.0);
            notasMaximas.put(resposta.getProva().getId(), notaMaxima);
        }

        // 5. Envia os dados para a página HTML
        model.addAttribute("respostas", minhasRespostas);
        model.addAttribute("notasMaximas", notasMaximas);

        return "aluno/index";
    }
}
