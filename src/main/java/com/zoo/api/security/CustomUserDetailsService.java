package com.zoo.api.security;

import com.zoo.api.entities.Adult;
import com.zoo.api.repositories.AdultRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdultRepository adultRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Adult adult = adultRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Tentative de connexion échouée : utilisateur non trouvé avec email '{}'", email);
                    return new UsernameNotFoundException("Utilisateur non trouvé");
                });

        // Si le rôle est null, l’adulte n’a aucun droit spécifique
        if (adult.getRole() == null) {
            return new User(
                adult.getEmail(),
                adult.getPassword(),
                Collections.emptyList() // aucune autorité
            );
        }

        // Sinon, on donne l'autorité ROLE_...
        return new User(
            adult.getEmail(),
            adult.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + adult.getRole().name()))
        );
    }
}
