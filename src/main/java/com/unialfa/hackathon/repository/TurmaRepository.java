package com.unialfa.hackathon.repository;

import com.unialfa.hackathon.model.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    boolean existsByNomeAndAnoLetivo(String nome, String anoLetivo);
}
