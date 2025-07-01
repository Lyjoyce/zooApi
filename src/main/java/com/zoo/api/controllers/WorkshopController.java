package com.zoo.api.controllers;

import com.zoo.api.entities.Workshop;
import com.zoo.api.services.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workshops")
@RequiredArgsConstructor
public class WorkshopController {

    private final WorkshopService workshopService;

    @GetMapping
    public ResponseEntity<List<Workshop>> getAllWorkshops() {
        return ResponseEntity.ok(workshopService.getAllWorkshops());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workshop> getWorkshopById(@PathVariable Long id) {
        return workshopService.getWorkshopById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Workshop> createWorkshop(@RequestBody Workshop workshop) {
        return ResponseEntity.ok(workshopService.saveWorkshop(workshop));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workshop> updateWorkshop(@PathVariable Long id, @RequestBody Workshop updatedWorkshop) {
        return workshopService.getWorkshopById(id)
                .map(ws -> {
                    ws.setDate(updatedWorkshop.getDate());
                    ws.setType(updatedWorkshop.getType());
                    ws.setEmployee(updatedWorkshop.getEmployee());
                    ws.setReservation(updatedWorkshop.getReservation());
                    ws.setUsedEgg(updatedWorkshop.getUsedEgg());
                    return ResponseEntity.ok(workshopService.saveWorkshop(ws));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkshop(@PathVariable Long id) {
        workshopService.deleteWorkshop(id);
        return ResponseEntity.noContent().build();
    }
}
