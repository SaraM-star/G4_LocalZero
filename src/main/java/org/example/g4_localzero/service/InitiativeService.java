package org.example.g4_localzero.service;

import org.example.g4_localzero.model.Initiative;
import org.example.g4_localzero.repository.InitiativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitiativeService {

    private final InitiativeRepository initiativeRepository;

    public InitiativeService(InitiativeRepository initiativeRepository) {
        this.initiativeRepository = initiativeRepository;
    }

    public List<Initiative> getAllInitiatives() {
        return initiativeRepository.findAll();
    }

    public Initiative createInitiative(Initiative initiative) {
        return initiativeRepository.save(initiative);
    }
}