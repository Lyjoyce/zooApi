package com.zoo.api.security;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zoo.api.repositories.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
            .map(account -> new User(
                account.getEmail(),
                account.getPassword(),
                account.isActive(), // enabled
                true,               // accountNonExpired
                true,               // credentialsNonExpired
                true,               // accountNonLocked
                Collections.singletonList(
                	    new SimpleGrantedAuthority("ROLE_" + account.getRole().name())// ajout de ROLE 
                	)
            ))
            .orElseThrow(() -> {
                logger.warn("Tentative de connexion échouée : utilisateur non trouvé avec email '{}'", email);
                return new UsernameNotFoundException("Utilisateur non trouvé");
            });
    }
}
