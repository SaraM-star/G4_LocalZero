package org.example.g4_localzero.service.eco;

public class PublicTransportEcoAction extends EcoActionTemplate{

    @Override
    protected double calculateCo2Saved(double kilometers){
        return kilometers * 0.1;
    }

    @Override
    protected String getActionType(){
        return "PUBLIC_TRANSPORT";
    }
}
