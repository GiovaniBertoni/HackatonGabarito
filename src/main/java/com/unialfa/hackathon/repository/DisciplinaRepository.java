package com.unialfa.hackathon.repository;

import com.unialfa.hackathon.model.entity.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    boolean existsByNome(String nome);

}
