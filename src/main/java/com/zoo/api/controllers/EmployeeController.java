package com.zoo.api.controllers;

import com.zoo.api.entities.Employee;
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

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.saveEmployee(employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        return employeeService.getEmployeeById(id)
                .map(emp -> {
                    emp.setFirstName(updatedEmployee.getFirstName());
                    emp.setLastName(updatedEmployee.getLastName());
                    return ResponseEntity.ok(employeeService.saveEmployee(emp));
                }).orElse(ResponseEntity.notFound().build());
    }

    // Soft delete : désactivation de l'employé
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Employee> deactivateEmployee(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(emp -> {
                    emp.setActive(false);
                    return ResponseEntity.ok(employeeService.saveEmployee(emp));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // Pour supprimer vraiment, il faut appeler employeeService.deleteEmployee(id);
    // @DeleteMapping("/{id}") public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {...}
}
