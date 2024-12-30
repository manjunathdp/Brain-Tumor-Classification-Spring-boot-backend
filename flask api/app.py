from flask import Flask, request, jsonify
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
import numpy as np
import os

# Initialize Flask app
app = Flask(__name__)

# Load the trained model
MODEL_PATH = 'densenet_brain_tumor.h5'
model = load_model(MODEL_PATH)

# Define class labels
CLASS_LABELS = ['glioma', 'meningioma', 'no_tumar', 'pituitary']

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Check if a file is part of the request
        if 'file' not in request.files:
            return jsonify({'error': 'No file uploaded'}), 400

        file = request.files['file']

        if file.filename == '':
            return jsonify({'error': 'No file selected'}), 400

        # Save the file temporarily
        temp_path = os.path.join('temp.jpg')
        file.save(temp_path)

        # Preprocess the image
        img = image.load_img(temp_path, target_size=(224, 224))
        img_array = image.img_to_array(img)
        img_array = np.expand_dims(img_array, axis=0)
        img_array /= 255.0  # Rescale pixel values

        # Make predictions
        predictions = model.predict(img_array)
        predicted_probabilities = predictions[0]

        # Create a dictionary with class probabilities
        predicted_results = dict(zip(CLASS_LABELS, predicted_probabilities))

        # Get the predicted class
        predicted_class = CLASS_LABELS[np.argmax(predicted_probabilities)]

        # Cleanup temporary file
        os.remove(temp_path)

        # Return the prediction result
        return jsonify({
            'predicted_class': predicted_class,
            'predicted_probabilities': predicted_results
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
