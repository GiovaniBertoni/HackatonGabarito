package com.unialfa.hackathon.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

@Data
public class RespostaRequestDto {
    @NotNull(message = "O ID da prova é obrigatório.")
    private Long provaId;

    @NotNull(message = "O ID do aluno é obrigatório.")
    private Long alunoId;

    @NotEmpty(message = "A lista de respostas não pode ser vazia.")
    private Map<Integer, String> respostas;
}
