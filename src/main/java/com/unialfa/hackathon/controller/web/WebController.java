package com.unialfa.hackathon.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Ponto de entrada principal da aplicação (rota "/").
     * Redireciona o usuário para o painel correto com base no seu perfil,
     * garantindo que a página inicial seja sempre a correta para quem está logado.
     */
    @GetMapping("/")
    public String home() {
        // Pega a informação do usuário atualmente autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            // Verifica se o usuário tem o perfil ADMIN
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin";
            }
            // Verifica se o usuário tem o perfil PROFESSOR
            else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"))) {
                return "redirect:/professor";
            }
        }

        // Se por algum motivo não encontrar um perfil, volta para o login.
        return "redirect:/login";
    }
}
