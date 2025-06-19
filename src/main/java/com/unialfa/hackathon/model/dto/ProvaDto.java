package com.unialfa.hackathon.model.dto;

import com.unialfa.hackathon.model.entity.Prova;
import lombok.Getter;

@Getter
public class ProvaDto {
    private Long id;
    private String titulo;
    private int numeroDeQuestoes;

    // Construtor que transforma uma Prova em um ProvaDto
    public ProvaDto(Prova prova, int numeroDeQuestoes) {
        this.id = prova.getId();
        this.titulo = prova.getTitulo() + " (" + prova.getTurma().getNome() + ")";
        this.numeroDeQuestoes = numeroDeQuestoes;
    }
}
