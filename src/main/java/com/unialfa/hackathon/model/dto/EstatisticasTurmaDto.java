package com.unialfa.hackathon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticasTurmaDto {
    private double media;
    private double maiorNota;
    private double menorNota;
    private long totalDeRespostas;
}