package org.example.g4_localzero.service.eco;

import org.example.g4_localzero.model.EcoAction;
import org.example.g4_localzero.model.User;

import java.time.LocalDateTime;

public abstract class EcoActionTemplate {

    public final EcoAction createEcoAction(User user, double amount) {
        validateAmount(amount);

        double co2Saved = calculateCo2Saved(amount);

        return EcoAction.builder()
                .user(user)
                .actionType(getActionType())
                .amount(amount)
                .co2Saved(co2Saved)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private void validateAmount(double amount){
        if (amount <=0){
            throw new IllegalArgumentException("Amount must be greater than 0");

        }
    }

    protected abstract double calculateCo2Saved(double amount);
    protected abstract String getActionType();
}
