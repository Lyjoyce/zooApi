package com.zoo.api.controllers;

import com.zoo.api.entities.Adult;
import com.zoo.api.services.AdultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adults")
@RequiredArgsConstructor
public class AdultController {

    private final AdultService adultService;

    @GetMapping
    public ResponseEntity<List<Adult>> getAllAdults() {
        return ResponseEntity.ok(adultService.getAllActiveAdults());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adult> getAdultById(@PathVariable Long id) {
        return adultService.getAdultById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Adult> createAdult(@RequestBody Adult adult) {
        return ResponseEntity.ok(adultService.saveAdult(adult));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adult> updateAdult(@PathVariable Long id, @RequestBody Adult updatedAdult) {
        return adultService.getAdultById(id)
                .map(adult -> {
                    adult.setFirstName(updatedAdult.getFirstName());
                    adult.setLastName(updatedAdult.getLastName());
                    adult.setEmail(updatedAdult.getEmail());
                    adult.setPhone(updatedAdult.getPhone());
                    adult.setType(updatedAdult.getType());
                    return ResponseEntity.ok(adultService.saveAdult(adult));
                }).orElse(ResponseEntity.notFound().build());
    }

    // Désactivation (soft delete)
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Adult> deactivateAdult(@PathVariable Long id) {
        return adultService.deactivateAdult(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
