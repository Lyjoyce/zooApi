package com.zoo.api.security;

import com.zoo.api.entities.Adult;
import com.zoo.api.repositories.AdultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdultRepository adultRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Adult adult = adultRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        return new User(
                adult.getEmail(),
                adult.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + adult.getRole().name()))
        );
    }
}

