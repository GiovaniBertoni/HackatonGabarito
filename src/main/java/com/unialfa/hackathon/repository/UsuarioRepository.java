package com.unialfa.hackathon.repository;

import com.unialfa.hackathon.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo seu endereço de e-mail.
     * Essencial para o processo de login no Spring Security.
     * @param email O e-mail do usuário.
     * @return um Optional contendo o Usuário se encontrado.
     */
    Optional<Usuario> findByEmail(String email);
}
