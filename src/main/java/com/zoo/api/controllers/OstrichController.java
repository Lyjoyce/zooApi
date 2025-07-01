package com.zoo.api.controllers;

import com.zoo.api.entities.Ostrich;
import com.zoo.api.services.OstrichService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ostriches")
@RequiredArgsConstructor
public class OstrichController {

    private final OstrichService ostrichService;

    @GetMapping
    public ResponseEntity<List<Ostrich>> getAllOstriches() {
        return ResponseEntity.ok(ostrichService.getAllActiveOstriches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ostrich> getOstrichById(@PathVariable Long id) {
        return ostrichService.getOstrichById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ostrich> createOstrich(@RequestBody Ostrich ostrich) {
        return ResponseEntity.ok(ostrichService.saveOstrich(ostrich));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ostrich> updateOstrich(@PathVariable Long id, @RequestBody Ostrich updatedOstrich) {
        return ostrichService.getOstrichById(id)
                .map(ostrich -> {
                    ostrich.setName(updatedOstrich.getName());
                    ostrich.setAge(updatedOstrich.getAge());
                    ostrich.setGender(updatedOstrich.getGender());
                    return ResponseEntity.ok(ostrichService.saveOstrich(ostrich));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOstrich(@PathVariable Long id) {
        ostrichService.deleteOstrich(id);
        return ResponseEntity.noContent().build();
    }
}

