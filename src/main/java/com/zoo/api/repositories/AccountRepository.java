package com.zoo.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zoo.api.entities.Account;
import com.zoo.api.enums.Role;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);

    // Vérifie si un compte avec le rôle donné existe
    boolean existsByRole(Role role);

    // Compte les comptes dont le rôle est dans la liste
    long countByRoleIn(List<Role> roles);
}
