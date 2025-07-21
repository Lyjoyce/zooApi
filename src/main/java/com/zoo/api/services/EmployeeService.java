package com.zoo.api.services;

import com.zoo.api.documents.Avis;
import com.zoo.api.entities.Employee;
import com.zoo.api.repositories.AvisRepository;
import com.zoo.api.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AvisRepository avisRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public Optional<Employee> updateEmployee(Long id, Employee updatedEmployee) {
        return employeeRepository.findById(id)
                .map(emp -> {
                    emp.setFirstName(updatedEmployee.getFirstName());
                    emp.setLastName(updatedEmployee.getLastName());
                    // Ajouter ici la mise à jour des autres champs si nécessaire
                    return employeeRepository.save(emp);
                });
    }

    public Optional<Employee> deactivateEmployee(Long id) {
        return employeeRepository.findById(id)
                .map(emp -> {
                    emp.setActive(false);
                    return employeeRepository.save(emp);
                });
    }

    public Avis validerAvis(String avisId) {
        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Avis non trouvé avec l'id : " + avisId));

        if (avis.isValidated()) {
            throw new IllegalStateException("⚠️ Cet avis est déjà validé.");
        }

        avis.setValidated(true);
        return avisRepository.save(avis);
    }
}
