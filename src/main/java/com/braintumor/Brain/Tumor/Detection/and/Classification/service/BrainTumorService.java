package com.braintumor.Brain.Tumor.Detection.and.Classification.service;

import com.braintumor.Brain.Tumor.Detection.and.Classification.dto.PredictionResponse;
import com.braintumor.Brain.Tumor.Detection.and.Classification.util.ImagePreprocessor;
import com.braintumor.Brain.Tumor.Detection.and.Classification.util.TensorFlowPayloadBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class BrainTumorService {

    private static final String MODEL_URL = "http://localhost:8501/v1/models/densenet_brain_tumor:predict";
    private static final String[] CLASS_LABELS = {"glioma", "meningioma", "no_tumor", "pituitary"};

    public PredictionResponse predict(MultipartFile file) throws IOException {
        // Preprocess the image
        byte[] processedImage = ImagePreprocessor.preprocessImage(file);

        // Prepare JSON payload for TensorFlow Serving
        String payload = TensorFlowPayloadBuilder.preparePayload(processedImage);

        // Call TensorFlow Serving
        HttpURLConnection connection = (HttpURLConnection) new URL(MODEL_URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        connection.getOutputStream().write(payload.getBytes());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode response = mapper.readTree(connection.getInputStream());

        // Parse response
        JsonNode predictionsArray = response.get("predictions").get(0); // Get first prediction
        if (predictionsArray == null || predictionsArray.size() != CLASS_LABELS.length) {
            throw new IOException("Invalid response from model.");
        }

        // Map class labels to probabilities
        Map<String, Double> labeledPredictions = new HashMap<>();
        double highestProbability = -1.0;
        String highestClass = null;

        for (int i = 0; i < CLASS_LABELS.length; i++) {
            double probability = predictionsArray.get(i).asDouble();
            labeledPredictions.put(CLASS_LABELS[i], probability);
            if (probability > highestProbability) {
                highestProbability = probability;
                highestClass = CLASS_LABELS[i];
            }
        }

        // Build the response object
        Map<String, Double> highestProbabilityMap = new HashMap<>();
        highestProbabilityMap.put(highestClass, highestProbability);

        return new PredictionResponse(labeledPredictions, highestProbabilityMap);
    }
}
