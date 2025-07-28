package com.zoo.api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.api.dtos.EggsPerDayDTO;
import com.zoo.api.dtos.EmployeeRequest;
import com.zoo.api.entities.Employee;
import com.zoo.api.services.AdminService;


@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired private AdminService adminService;
    
    
    @PostMapping("/employees/register")
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeRequest request) {
        Employee emp = adminService.createEmployee(request.getFirstName(),request.getLastName(),request.getEmail(),request.getPassword());
        return ResponseEntity.ok(emp);
    }

    @PostMapping("/suspend/{accountId}")
    public ResponseEntity<Void> suspendAccount(@PathVariable Long accountId) {
        adminService.suspendAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats/eggs")
    public ResponseEntity<List<EggsPerDayDTO>> eggsPerDay() {
        return ResponseEntity.ok(adminService.getEggsPerDay());
    }

    @GetMapping("/stats/omelettes")
    public ResponseEntity<List<Map<String, Object>>> omeletteStats() {
        return ResponseEntity.ok(adminService.getOmeletteWorkshopsPerDay());
    }
    
    @GetMapping("employees/veterinarians")
    public List<Employee> getVeterinarians() {
        return adminService.getVeterinarians();
    }
}

