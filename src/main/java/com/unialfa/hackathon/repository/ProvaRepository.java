package com.unialfa.hackathon.repository;

import com.unialfa.hackathon.model.entity.Prova;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvaRepository extends JpaRepository<Prova, Long> {

    /**
     * Busca todas as provas associadas a um professor específico.
     * @param professorId O ID do usuário professor.
     * @return Uma lista de provas.
     */
    List<Prova> findByProfessorId(Long professorId);

    /**
     * Busca todas as provas de uma determinada turma.
     * @param turmaId O ID da turma.
     * @return Uma lista de provas.
     */
    List<Prova> findByTurmaId(Long turmaId);
}
