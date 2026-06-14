package org.example.g4_localzero.service;

import lombok.RequiredArgsConstructor;
import org.example.g4_localzero.dto.EcoActionRequest;
import org.example.g4_localzero.model.EcoAction;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.repository.EcoActionRepository;
import org.example.g4_localzero.service.eco.BikeEcoAction;
import org.example.g4_localzero.service.eco.EcoActionTemplate;
import org.example.g4_localzero.service.eco.PublicTransportEcoAction;
import org.example.g4_localzero.service.eco.RecycleEcoAction;
import org.example.g4_localzero.service.eco.VegetarianMealEcoAction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EcoActionService {
    private final EcoActionRepository ecoActionRepository;

    public EcoAction logAction(EcoActionRequest request, User user) {
        EcoActionTemplate template = getTemplate(request.getActionType());

        EcoAction ecoAction = template.createEcoAction(user, request.getAmount());

        return ecoActionRepository.save(ecoAction);
    }

    public List<EcoAction> getActionsForUser(User user) {
        return ecoActionRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public double getTotalCo2Saved(User user) {
        return getActionsForUser(user)
                .stream()
                .mapToDouble(EcoAction::getCo2Saved)
                .sum();
    }

    private EcoActionTemplate getTemplate(String actionType) {
        return switch (actionType) {
            case "BIKE" -> new BikeEcoAction();
            case "VEGETARIAN_MEAL" -> new VegetarianMealEcoAction();
            case "RECYCLE" -> new RecycleEcoAction();
            case "PUBLIC_TRANSPORT" -> new PublicTransportEcoAction();
            default -> throw new IllegalArgumentException("Unknown eco action type: " + actionType);
        };
    }
}

