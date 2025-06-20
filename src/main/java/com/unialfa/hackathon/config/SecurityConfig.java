package com.unialfa.hackathon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Regras de permissão ordenadas da mais específica para a mais geral >>>

                        // 1. Permite o acesso à consola H2 APENAS para ADMINs
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/banco/**")).hasRole("ADMIN")

                        // 2. Permite o acesso à API para qualquer utilizador autenticado
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).authenticated()

                        // 3. Permite acesso público a ficheiros estáticos e à página de login
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll()

                        // 4. Regras específicas por perfil (embora @PreAuthorize nos controllers já faça isto)
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/professor/**").hasRole("PROFESSOR")
                        .requestMatchers("/aluno/**").hasRole("ALUNO")

                        // 5. Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // <<< MUDANÇA: Configurações para a consola H2 movidas para aqui >>>
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/banco/**")) // Desativa CSRF para a consola
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/**")) // Desativa CSRF para a API
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Permite que a consola seja renderizada num frame
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