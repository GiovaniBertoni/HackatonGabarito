package com.unialfa.hackathon.controller.api;

import com.unialfa.hackathon.model.dto.RespostaRequestDto;
import com.unialfa.hackathon.service.CorrecaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/respostas")
public class CorrecaoApiController {

    @Autowired
    private CorrecaoService correcaoService;

    @PostMapping
    public ResponseEntity<Void> receberRespostas(@Valid @RequestBody RespostaRequestDto respostaDto) {
        correcaoService.processarRespostas(respostaDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
