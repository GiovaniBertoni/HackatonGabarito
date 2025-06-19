package com.unialfa.hackathon.service;

import com.unialfa.hackathon.model.entity.Usuario;
import com.unialfa.hackathon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Localiza um usuário pelo seu nome de usuário (que, neste caso, é o e-mail).
     * Este método é chamado pelo Spring Security durante o processo de autenticação.
     *
     * @param email o e-mail do usuário a ser localizado.
     * @return um objeto UserDetails contendo as informações do usuário encontrado.
     * @throws UsernameNotFoundException se nenhum usuário for encontrado com o e-mail fornecido.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Busca o usuário no banco de dados pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        // 2. Converte o Perfil do nosso usuário (ADMIN, PROFESSOR, ALUNO) em uma "Authority" que o Spring Security entende.
        // O prefixo "ROLE_" é uma convenção do Spring Security.
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name())
        );

        // 3. Retorna um objeto User do Spring Security.
        // Este objeto encapsula o nome de usuário (email), a senha (já codificada) e as permissões (authorities).
        // O Spring Security usará isso para comparar com os dados fornecidos no login.
        return new User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}
