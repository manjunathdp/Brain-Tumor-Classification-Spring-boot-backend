# Brain Tumor Detection and Classification Backend

## Overview
This project provides a backend for brain tumor detection and classification using a deep learning model deployed via TensorFlow Serving. It leverages Spring Boot for REST API development and integrates with a pre-trained model for inference.

---

## Technology Stack
- **Backend Framework**: Spring Boot (Java)
- **Deep Learning Framework**: TensorFlow
- **Model Deployment**: TensorFlow Serving (Docker)
- **Data Interchange**: REST APIs with JSON payloads
- **Model Input**: 224x224 RGB images
- **Model Output**: Predicted probabilities for tumor classes

---

## Project Structure
### **Maven Configuration (`pom.xml`):**
- **Dependencies**:
    - `spring-boot-starter-web`: RESTful web services.
    - `jackson-databind`: JSON parsing and serialization.
- **Plugins**:
    - `maven-compiler-plugin`: Java compiler with Lombok support.
    - `spring-boot-maven-plugin`: For building and running the application.
- **Java Version**: Configured for Java 21.

### **Key Components**
#### 1. Controller (`BrainTumorController`):
- **Endpoint**: `/api/v1/predict`
    - **HTTP Method**: POST
    - **Input**: Multipart image file.
    - **Output**: JSON response containing:
        - Predicted probabilities for each class.
        - The class with the highest probability.

#### 2. Service (`BrainTumorService`):
- **Responsibilities**:
    - Preprocesses images to the required model format.
    - Builds the JSON payload for TensorFlow Serving.
    - Calls the TensorFlow Serving API and processes the response.
    - Maps predictions to tumor class labels.

#### 3. Utilities:
- **`ImagePreprocessor`**:
    - Converts images to RGB format if necessary.
    - Resizes images to 224x224.
    - Normalizes pixel values.
- **`TensorFlowPayloadBuilder`**:
    - Constructs JSON payload for TensorFlow Serving.

#### 4. Data Transfer Object (`PredictionResponse`):
- Encapsulates:
    - `predictions`: Map of class labels to probabilities.
    - `highestProbability`: The class with the highest probability.

---

## Model Deployment
### **TensorFlow Serving with Docker**
1. **Directory Structure**:
    - Local path: `H:/Projects/ai_ml/densenet_brain_tumor_savedmodel`
    - Must contain TensorFlow SavedModel files (`saved_model.pb`, variables folder).

2. **Docker Command**:
   ```bash
   docker run -p 8501:8501 --name tf_serving \
   -v "H:/Projects/ai_ml/densenet_brain_tumor_savedmodel:/models/densenet_brain_tumor" \
   -e MODEL_NAME=densenet_brain_tumor tensorflow/serving
   ```
    - **Flags**:
        - `-p 8501:8501`: Maps port 8501 on Docker to host.
        - `-v`: Mounts the local model directory to Docker.
        - `-e MODEL_NAME`: Specifies model name for TensorFlow Serving.

3. **Endpoint**:
    - URL: `http://localhost:8501/v1/models/densenet_brain_tumor:predict`
    - **Input**: JSON payload with preprocessed image.
    - **Output**: Probabilities for each class.

---

## Workflow
1. **Client Interaction**:
    - Uploads an image via `/api/v1/predict` endpoint.
2. **Image Processing**:
    - Image is converted to 224x224 RGB format with normalized pixel values.
3. **Model Inference**:
    - Preprocessed image is sent to TensorFlow Serving.
    - Predictions are received and parsed.
4. **Response**:
    - Returns prediction probabilities and the class with the highest probability.

---

## Example Payloads
### **Input Payload**:
```json
{
  "instances": [
    {
      "input_1": [[[0.5, 0.6, 0.7], ...]] // 224x224x3 normalized pixel values
    }
  ]
}
```

### **Response Example**:
```json
{
  "predictions": {
    "meningioma": 1.83138559E-6,
    "pituitary": 1.48122908E-5,
    "glioma": 0.998997271,
    "no_tumor": 9.86210885E-4
  },
  "highestProbability": {
    "glioma": 0.998997271
  }
}
```

---


## Additional Notes
- Ensure the model directory contains the proper TensorFlow SavedModel format.
- The backend assumes the TensorFlow Serving API is reachable at `http://localhost:8501`.
- Docker must be installed and running on the host system.

---

## Future Work
- Extend the API to support batch predictions.
- Improve logging and monitoring for better observability.
- Integrate with cloud services (e.g., AWS, GCP) for production deployment.
