package com.zoo.api.controllers;

import com.zoo.api.documents.Avis;
import com.zoo.api.entities.Employee;
import com.zoo.api.services.AvisService;
import com.zoo.api.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final AvisService avisService;

    // GET : Récupérer tous les employés
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // GET : Récupérer un employé par ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST : Créer un nouvel employé
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.ok(createdEmployee);
    }

    // PUT : Mettre à jour un employé existant
    @PutMapping("/id/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        return employeeService.updateEmployee(id, updatedEmployee)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT : Désactiver (soft delete) un employé
    @PutMapping("/id/{id}/deactivate")
    public ResponseEntity<Employee> deactivateEmployee(@PathVariable Long id) {
        return employeeService.deactivateEmployee(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

 // PUT : Valider un avis via son id (retour unifié {message + avis})
    @PutMapping("/{id}/validate")
    public ResponseEntity<?> validerAvisParEmployee(@PathVariable String id) {
        return avisService.validerAvis(id);
    }
}
