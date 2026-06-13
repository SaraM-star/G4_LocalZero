package org.example.g4_localzero.service.eco;

public class RecycleEcoAction extends EcoActionTemplate{

    @Override
    protected double calculateCo2Saved(double kilograms){
        return kilograms * 0.7;
    }

    @Override
    protected String getActionType(){
        return "RECYCLE";
    }
}
