package com.unialfa.hackathon.repository;

import com.unialfa.hackathon.model.entity.Gabarito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GabaritoRepository extends JpaRepository<Gabarito, Long> {

    /**
     * Busca um gabarito com base no ID da prova associada.
     * @param provaId O ID da prova.
     * @return um Optional contendo o Gabarito se encontrado.
     */
    Optional<Gabarito> findByProvaId(Long provaId);
}
