package com.unialfa.hackathon.controller.web;

import com.unialfa.hackathon.model.dto.EstatisticasTurmaDto; // <<< IMPORTAR
import com.unialfa.hackathon.model.entity.*;
import com.unialfa.hackathon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.DoubleSummaryStatistics; // <<< IMPORTAR
import java.util.Date;
import java.util.List; // <<< IMPORTAR
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/professor")
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorController {

    @Autowired private ProvaRepository provaRepository;
    @Autowired private TurmaRepository turmaRepository;
    @Autowired private DisciplinaRepository disciplinaRepository;
    @Autowired private GabaritoRepository gabaritoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RespostaAlunoRepository respostaAlunoRepository;

    @GetMapping
    public String professorPanel(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario professor = usuarioRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("provas", provaRepository.findByProfessorId(professor.getId()));
        return "professor/index";
    }

    @GetMapping("/provas/nova")
    public String exibirFormularioProva(Model model) {
        model.addAttribute("prova", new Prova());
        model.addAttribute("turmas", turmaRepository.findAll());
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        return "professor/form-prova";
    }

    @PostMapping("/provas")
    public String salvarProva(@ModelAttribute Prova prova, @RequestParam Map<String, String> allParams, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        Usuario professor = usuarioRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        prova.setProfessor(professor);
        prova.setDataAplicacao(new Date());

        Prova provaSalva = provaRepository.save(prova);

        Gabarito gabarito = new Gabarito();
        gabarito.setProva(provaSalva);

        Map<Integer, GabaritoQuestao> gabaritoFinal = new TreeMap<>();
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("resposta-")) {
                Integer questaoNum = Integer.parseInt(entry.getKey().replace("resposta-", ""));
                String resposta = entry.getValue();
                try {
                    // Adicionado .replace(",", ".") para aceitar tanto ponto quanto vírgula como separador decimal.
                    double valor = Double.parseDouble(allParams.getOrDefault("valor-" + questaoNum, "0.0").replace(",", "."));
                    gabaritoFinal.put(questaoNum, new GabaritoQuestao(resposta, valor));
                } catch (NumberFormatException e) { /* Ignora se o valor não for um número válido */ }
            }
        }
        gabarito.setRespostas(gabaritoFinal);
        gabaritoRepository.save(gabarito);

        redirectAttributes.addFlashAttribute("sucesso", "Prova e gabarito criados com sucesso!");
        return "redirect:/professor";
    }

    @GetMapping("/provas/{id}/resultados")
    public String verResultados(@PathVariable Long id, Model model) {
        Prova prova = provaRepository.findById(id).orElseThrow(() -> new RuntimeException("Prova não encontrada"));
        List<RespostaAluno> respostas = respostaAlunoRepository.findByProvaId(id);

        // Início do cálculo das estatísticas >>>
        EstatisticasTurmaDto estatisticas;
        if (respostas.isEmpty()) {
            estatisticas = new EstatisticasTurmaDto(0, 0, 0, 0);
        } else {
            DoubleSummaryStatistics stats = respostas.stream()
                    .mapToDouble(RespostaAluno::getNota)
                    .summaryStatistics();
            estatisticas = new EstatisticasTurmaDto(
                    stats.getAverage(),
                    stats.getMax(),
                    stats.getMin(),
                    stats.getCount()
            );
        }
        // <<< FIM do cálculo das estatísticas >>>
        double notaMaxima = gabaritoRepository.findByProvaId(id)
                .map(gabarito -> gabarito.getRespostas().values().stream()
                        .mapToDouble(GabaritoQuestao::getValor)
                        .sum())
                .orElse(0.0);

        model.addAttribute("prova", prova);
        model.addAttribute("respostas", respostas);
        model.addAttribute("notaMaxima", notaMaxima);
        model.addAttribute("estatisticas", estatisticas); // Envia as estatísticas para a página

        return "professor/resultados";
    }
}
