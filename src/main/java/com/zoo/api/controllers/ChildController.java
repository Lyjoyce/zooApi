package com.zoo.api.controllers;

import com.zoo.api.entities.Child;
import com.zoo.api.services.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    @GetMapping
    public ResponseEntity<List<Child>> getAllChildren() {
        return ResponseEntity.ok(childService.getAllActiveChildren());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Child> getChildById(@PathVariable Long id) {
        return childService.getChildById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Child> createChild(@RequestBody Child child) {
        return ResponseEntity.ok(childService.saveChild(child));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(@PathVariable Long id, @RequestBody Child updatedChild) {
        return childService.getChildById(id)
                .map(child -> {
                    child.setFirstName(updatedChild.getFirstName());
                    child.setLastName(updatedChild.getLastName());
                    child.setAge(updatedChild.getAge());
                    child.setSchoolClass(updatedChild.getSchoolClass());
                    child.setResponsibleAdult(updatedChild.getResponsibleAdult());
                    return ResponseEntity.ok(childService.saveChild(child));
                }).orElse(ResponseEntity.notFound().build());
    }

    // Soft delete : désactiver un enfant sans le supprimer réellement
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Child> deactivateChild(@PathVariable Long id) {
        return childService.deactivateChild(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
