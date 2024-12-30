package com.braintumor.Brain.Tumor.Detection.and.Classification.util;

import java.io.IOException;

public class TensorFlowPayloadBuilder {

    public static String preparePayload(byte[] imageBytes) throws IOException {
        System.out.println("Image bytes length: " + imageBytes.length);
        if (imageBytes.length != 224 * 224 * 3) {
            throw new IllegalArgumentException("Image data does not match the expected size of 224x224x3.");
        }

        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{\"instances\": [{\"input_1\": [");

        int index = 0;
        for (int i = 0; i < 224; i++) {
            payloadBuilder.append("[");
            for (int j = 0; j < 224; j++) {
                payloadBuilder.append("[");
                for (int k = 0; k < 3; k++) { // RGB channels
                    float normalizedValue = (imageBytes[index++] & 0xFF) / 255.0f;
                    payloadBuilder.append(normalizedValue);
                    if (k < 2) payloadBuilder.append(",");
                }
                payloadBuilder.append("]");
                if (j < 223) payloadBuilder.append(",");
            }
            payloadBuilder.append("]");
            if (i < 223) payloadBuilder.append(",");
        }

        payloadBuilder.append("]}]}");
        return payloadBuilder.toString();
    }

}
