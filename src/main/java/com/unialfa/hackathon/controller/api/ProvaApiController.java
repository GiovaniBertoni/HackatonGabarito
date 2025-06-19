package com.unialfa.hackathon.controller.api;

import com.unialfa.hackathon.model.dto.ProvaDto;
import com.unialfa.hackathon.model.entity.Usuario;
import com.unialfa.hackathon.repository.GabaritoRepository;
import com.unialfa.hackathon.repository.ProvaRepository;
import com.unialfa.hackathon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/provas")
public class ProvaApiController {

    @Autowired private ProvaRepository provaRepository;
    @Autowired private GabaritoRepository gabaritoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<ProvaDto>> listarProvasDoProfessor(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario professor = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        List<ProvaDto> provas = provaRepository.findByProfessorId(professor.getId()).stream()
                .map(prova -> {
                    int numeroDeQuestoes = gabaritoRepository.findByProvaId(prova.getId())
                            .map(gabarito -> gabarito.getRespostas().size())
                            .orElse(0);
                    return new ProvaDto(prova, numeroDeQuestoes);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(provas);
    }
}
