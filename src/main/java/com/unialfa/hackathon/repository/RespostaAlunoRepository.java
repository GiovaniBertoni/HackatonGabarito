package com.unialfa.hackathon.repository;

import com.unialfa.hackathon.model.entity.RespostaAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespostaAlunoRepository extends JpaRepository<RespostaAluno, Long> {

    /**
     * Busca todas as respostas de uma prova específica.
     * Útil para calcular estatísticas da turma.
     * @param provaId O ID da prova.
     * @return Uma lista de respostas dos alunos para aquela prova.
     */
    List<RespostaAluno> findByProvaId(Long provaId);

    /**
     * Busca todas as respostas submetidas por um aluno específico.
     * Útil para a tela de consulta de notas do aluno.
     * @param alunoId O ID do aluno.
     * @return Uma lista de respostas daquele aluno.
     */
    List<RespostaAluno> findByAlunoId(Long alunoId);

    boolean existsByAlunoId(Long id);
}
