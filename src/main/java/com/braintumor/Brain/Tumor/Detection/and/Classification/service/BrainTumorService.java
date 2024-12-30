package com.braintumor.Brain.Tumor.Detection.and.Classification.service;

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

    public Map<String, Object> predict(MultipartFile file) throws IOException {
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

        // Parse response and return results
        Map<String, Object> results = new HashMap<>();
        JsonNode predictions = response.get("predictions");
        results.put("predictions", predictions);

        return results;
    }
}