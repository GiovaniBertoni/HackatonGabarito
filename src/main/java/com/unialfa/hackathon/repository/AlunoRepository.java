package com.unialfa.hackathon.repository;

import com.unialfa.hackathon.model.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Importe, se necessário

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByUsuarioId(Long usuarioId);

    Optional<Object> findByRa(String ra);
}
