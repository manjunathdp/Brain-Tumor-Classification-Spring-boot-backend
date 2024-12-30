package com.braintumor.Brain.Tumor.Detection.and.Classification.dto;

import java.util.Map;

public class PredictionResponse {

    private Map<String, Double> predictions;
    private Map<String, Double> highestProbability;

    public PredictionResponse(Map<String, Double> predictions, Map<String, Double> highestProbability) {
        this.predictions = predictions;
        this.highestProbability = highestProbability;
    }

    public Map<String, Double> getPredictions() {
        return predictions;
    }

    public void setPredictions(Map<String, Double> predictions) {
        this.predictions = predictions;
    }

    public Map<String, Double> getHighestProbability() {
        return highestProbability;
    }

    public void setHighestProbability(Map<String, Double> highestProbability) {
        this.highestProbability = highestProbability;
    }
}
