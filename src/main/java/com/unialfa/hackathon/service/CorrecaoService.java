package com.unialfa.hackathon.service;

import com.unialfa.hackathon.model.dto.RespostaRequestDto;
import com.unialfa.hackathon.model.entity.Gabarito;
import com.unialfa.hackathon.model.entity.GabaritoQuestao;
import com.unialfa.hackathon.model.entity.RespostaAluno;
import com.unialfa.hackathon.repository.AlunoRepository;
import com.unialfa.hackathon.repository.GabaritoRepository;
import com.unialfa.hackathon.repository.ProvaRepository;
import com.unialfa.hackathon.repository.RespostaAlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CorrecaoService {

    @Autowired private RespostaAlunoRepository respostaAlunoRepository;
    @Autowired private GabaritoRepository gabaritoRepository;
    @Autowired private ProvaRepository provaRepository;
    @Autowired private AlunoRepository alunoRepository;

    public void processarRespostas(RespostaRequestDto requestDto) {
        var prova = provaRepository.findById(requestDto.getProvaId()).orElseThrow(() -> new RuntimeException("Prova não encontrada"));
        var aluno = alunoRepository.findById(requestDto.getAlunoId()).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Gabarito gabarito = gabaritoRepository.findByProvaId(prova.getId()).orElseThrow(() -> new RuntimeException("Gabarito para a prova não encontrado"));

        double nota = calcularNotaComPesos(requestDto.getRespostas(), gabarito.getRespostas());

        RespostaAluno respostaAluno = new RespostaAluno();
        respostaAluno.setProva(prova);
        respostaAluno.setAluno(aluno);
        respostaAluno.setRespostas(requestDto.getRespostas());
        respostaAluno.setNota(nota);

        respostaAlunoRepository.save(respostaAluno);
    }
    private double calcularNotaComPesos(Map<Integer, String> respostasAluno, Map<Integer, GabaritoQuestao> gabarito) {
        double notaFinal = 0.0;

        for (Map.Entry<Integer, String> respostaDoAluno : respostasAluno.entrySet()) {
            Integer questaoNum = respostaDoAluno.getKey();
            String respostaAluno = respostaDoAluno.getValue();
            GabaritoQuestao questaoOficial = gabarito.get(questaoNum);

            if (questaoOficial != null && questaoOficial.getResposta().equalsIgnoreCase(respostaAluno)) {
                notaFinal += questaoOficial.getValor();
            }
        }
        return notaFinal;
    }
}
