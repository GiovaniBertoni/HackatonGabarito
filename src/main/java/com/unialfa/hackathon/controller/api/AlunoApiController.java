package com.unialfa.hackathon.controller.api;

import com.unialfa.hackathon.repository.AlunoRepository;
import com.unialfa.hackathon.model.dto.AlunoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/alunos")
public class AlunoApiController {

    @Autowired
    private AlunoRepository alunoRepository;

    @GetMapping
    public ResponseEntity<List<AlunoDto>> listarAlunos() {
        List<AlunoDto> alunos = alunoRepository.findAll().stream()
                .map(AlunoDto::new) // Converte a entidade Aluno para um DTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(alunos);
    }
}
