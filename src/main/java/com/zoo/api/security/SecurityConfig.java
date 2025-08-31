package com.zoo.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity // active @PreAuthorize
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // REST API, pas besoin de CSRF
            .authorizeHttpRequests(auth -> auth
                // -------------------
                // PUBLIC
                // -------------------
                .requestMatchers("/api/v1/admin/login").permitAll()
                .requestMatchers("/api/v1/employees/login").permitAll()
                .requestMatchers("/api/v1/avis").permitAll()
                
                // -------------------
                // RESTRICTIONS PAR RÔLES
                // -------------------
                .requestMatchers("/api/v1/veto/**").hasRole("VETERINAIRE")
                .requestMatchers("/api/v1/employees/**").hasRole("EMPLOYE")
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                // tout le reste nécessite un utilisateur connecté
                .requestMatchers("/test-mail").permitAll()
                .anyRequest().authenticated()
            )
            // Ajout du filtre JWT avant UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
