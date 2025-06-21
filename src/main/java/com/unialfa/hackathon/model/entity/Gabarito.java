package com.unialfa.hackathon.model.entity;

import com.unialfa.hackathon.model.converter.GabaritoQuestaoConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashMap; // <-- IMPORTAR
import java.util.Map;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Gabarito implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prova_id", nullable = false, unique = true)
    private Prova prova;

    // Inicializamos o mapa para que ele nunca seja nulo.
    @Convert(converter = GabaritoQuestaoConverter.class)
    @Column(columnDefinition = "TEXT", nullable = false)
    private Map<Integer, GabaritoQuestao> respostas = new HashMap<>();
}
