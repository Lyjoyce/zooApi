package com.zoo.api.security;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zoo.api.entities.Account;
import com.zoo.api.entities.Adult;
import com.zoo.api.repositories.AccountRepository;
import com.zoo.api.repositories.AdultRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdultRepository adultRepository;
    private final AccountRepository accountRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var adultOpt = adultRepository.findByEmail(email);
        if (adultOpt.isPresent()) {
            Adult adult = adultOpt.get();
         // Pas d'autorité Spring Security, liste vide
            return new User(
                adult.getEmail(),
                adult.getPassword(),
                adult.isActive(),
                true, true, true,
                Collections.emptyList()  // AUCUN ROLE ici
            );
        }

        var accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            return new User(
                account.getEmail(),
                account.getPassword(),
                account.isActive(),
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority(account.getRole().name()))
            );
        }

        logger.warn("Tentative de connexion échouée : utilisateur non trouvé avec email '{}'", email);
        throw new UsernameNotFoundException("Utilisateur non trouvé");
    }
}
