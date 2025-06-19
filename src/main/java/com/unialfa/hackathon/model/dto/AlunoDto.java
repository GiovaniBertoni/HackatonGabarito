package com.unialfa.hackathon.model.dto;

import com.unialfa.hackathon.model.entity.Aluno;
import lombok.Getter;

@Getter
public class AlunoDto {
    private Long id;
    private String nome;
    private String ra;

    // Construtor que converte a entidade Aluno em AlunoDto
    public AlunoDto(Aluno aluno) {
        this.id = aluno.getId();
        this.nome = aluno.getUsuario().getNome();
        this.ra = aluno.getRa();
    }
}
