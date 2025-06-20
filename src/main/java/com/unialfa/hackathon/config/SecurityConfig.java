package com.unialfa.hackathon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // Aplica esta regra APENAS para rotas que começam com /api/
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // Qualquer rota da API precisa de autenticação
                )
                .csrf(csrf -> csrf.disable()) // Desativa CSRF para a API, que é stateless
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Não cria sessão no servidor
                .httpBasic(withDefaults()); // Usa autenticação Básica (utilizador e senha em cada pedido)
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/css/**"),
                                AntPathRequestMatcher.antMatcher("/js/**"),
                                AntPathRequestMatcher.antMatcher("/images/**"),
                                AntPathRequestMatcher.antMatcher("/login")
                        ).permitAll() // Permite acesso público a ficheiros estáticos e à página de login
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/banco/**")).hasRole("ADMIN") // Regra para a consola do banco de dados
                        .anyRequest().authenticated() // Qualquer outra página web requer login
                )
                .formLogin(form -> form
                        .loginPage("/login") // Define a página de login customizada
                        .successHandler(customAuthenticationSuccessHandler()) // Redirecionamento inteligente após login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Para onde ir após o logout
                        .permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()) // Necessário para a consola H2 funcionar
                );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/admin");
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"))) {
                response.sendRedirect("/professor");
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ALUNO"))) {
                response.sendRedirect("/aluno");
            } else {
                response.sendRedirect("/");
            }
        };
    }
}
