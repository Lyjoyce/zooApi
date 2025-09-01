package com.zoo.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity // active @PreAuthorize
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // REST API, pas besoin de CSRF
            .cors(cors -> {}) // active CORS avec la config corsConfigurationSource()
            .authorizeHttpRequests(auth -> auth
                // -------------------
                // PUBLIC
                // -------------------
                .requestMatchers(HttpMethod.POST, "/api/v1/admin/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/employees/login").permitAll()
                .requestMatchers("/api/v1/avis").permitAll()
                .requestMatchers("/test-mail").permitAll()

                // -------------------
                // RESTRICTIONS PAR RÔLES
                // -------------------
                .requestMatchers("/api/v1/veto/**").hasRole("VETERINAIRE")
                .requestMatchers("/api/v1/employees/**").hasRole("EMPLOYE")
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                // tout le reste nécessite un utilisateur connecté
                .anyRequest().authenticated()
            )
            // Ajout du filtre JWT avant UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://lyjoyce.github.io")); // autoriser ton front
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // nécessaire pour envoyer JWT dans les headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // encodage sécurisé des mots de passe
    }

}
