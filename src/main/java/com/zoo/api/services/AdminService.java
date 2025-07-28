package com.zoo.api.services;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.zoo.api.entities.Account;
import com.zoo.api.entities.Employee;
import com.zoo.api.enums.Role;
import com.zoo.api.repositories.AccountRepository;
import com.zoo.api.repositories.EggRepository;
import com.zoo.api.repositories.EmployeeRepository;
import com.zoo.api.repositories.WorkshopRepository;
import com.zoo.api.security.JwtUtil;
import com.zoo.api.dtos.AdminLoginRequest;
import com.zoo.api.dtos.AdminLoginResponse;
import com.zoo.api.dtos.EggsPerDayDTO;

@Service
public class AdminService {

    @Autowired private AccountRepository accountRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private EggRepository eggRepository;
    @Autowired private WorkshopRepository workshopRepository;
    @Autowired private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
     

    public AdminLoginResponse loginAdmin(AdminLoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        if (account.getRole() != Role.ROLE_ADMIN) {
            throw new RuntimeException("Non autorisé : rôle administrateur requis.");
        }

        // Authentifie via Spring Security
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(account); // ✅ méthode correcte

        return new AdminLoginResponse(
            account.getId(),
            account.getEmail(),
            account.getFirstName(),
            account.getLastName(),
            token
        );
    }


    public Employee createEmployee(String firstName, String lastName, String email, String password) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password)); // à encoder avec BCrypt
        account.setRole(Role.ROLE_EMPLOYEE);
        account.setEnabled(true);
        accountRepository.save(account);

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setAccount(account);
        return employeeRepository.save(employee);
    }

    public void suspendAccount(Long accountId) {
        Account acc = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
        acc.setEnabled(false);
        accountRepository.save(acc);
    }

    public List<EggsPerDayDTO> getEggsPerDay() {
        return eggRepository.countEggsPerDayDTO();
    }

    public List<Map<String, Object>> getOmeletteWorkshopsPerDay() {
        List<Object[]> raw = workshopRepository.countOmeletteWorkshopsPerDay();
        return raw.stream().map(obj -> Map.of("date", obj[0], "count", obj[1])).toList();
    }
    
    public List<Employee> getVeterinarians() {
        return employeeRepository.findByAccountRole(Role.ROLE_VETERINAIRE);
    }
    
    public void suspendEmployeeAccount(Long employeeId) {
        Employee emp = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        Account acc = emp.getAccount();
        if (acc != null) {
            acc.setEnabled(false);
            accountRepository.save(acc);
        }
        emp.setActive(false);
        employeeRepository.save(emp);
    }
}

