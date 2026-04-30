package org.example.g4_localzero.controller;

import org.example.g4_localzero.model.Initiative;
import org.example.g4_localzero.service.InitiativeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/initiatives")
public class InitiativeController {

    private final InitiativeService initiativeService;

    public InitiativeController(InitiativeService initiativeService) {
        this.initiativeService = initiativeService;
    }

    @GetMapping
    public List<Initiative> getAllInitiatives() {
        return initiativeService.getAllInitiatives();
    }

    @PostMapping
    public Initiative createInitiative(@RequestBody Initiative initiative) {
        return initiativeService.createInitiative(initiative);
    }
}