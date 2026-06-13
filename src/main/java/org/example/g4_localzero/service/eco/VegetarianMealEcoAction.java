package org.example.g4_localzero.service.eco;

public class VegetarianMealEcoAction extends EcoActionTemplate{

    @Override
    protected double calculateCo2Saved(double meals){
        return meals * 1.5;
    }
    @Override
    protected String getActionType(){
        return "VEGETARIAN_MEAL";
    }
}
