package org.example.g4_localzero.service.eco;

public class BikeEcoAction extends EcoActionTemplate {

    @Override
    protected double calculateCo2Saved(double kilometers) {
        return kilometers * 0.2;
    }

    @Override
    protected String getActionType(){
    return"BIKE";
}
}

