package com.braintumor.Brain.Tumor.Detection.and.Classification.util;

public class TensorFlowPayloadBuilder {

    public static String preparePayload(byte[] imageBytes) {
        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{\"instances\": [{\"input_1\": [");
        for (byte b : imageBytes) {
            float normalizedValue = (b & 0xFF) / 255.0f; // Normalize byte to [0, 1]
            payloadBuilder.append(normalizedValue).append(",");
        }
        // Remove trailing comma and close JSON structure
        if (payloadBuilder.charAt(payloadBuilder.length() - 1) == ',') {
            payloadBuilder.setLength(payloadBuilder.length() - 1);
        }
        payloadBuilder.append("]}]}\n");
        return payloadBuilder.toString();
    }
}
