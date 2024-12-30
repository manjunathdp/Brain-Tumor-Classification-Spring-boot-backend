import tensorflow as tf
from keras.layers import TFSMLayer
from keras.utils import img_to_array
from PIL import Image
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import os

# Define the path to the SavedModel directory
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
MODEL_PATH = os.path.join(BASE_DIR, 'predictor/static/densenet_brain_tumor_savedmodel')

# Load the SavedModel using TFSMLayer
model_layer = TFSMLayer(MODEL_PATH, call_endpoint='serving_default')  # Adjust `call_endpoint` if necessary

# Define class labels
CLASS_LABELS = ['glioma', 'meningioma', 'no_tumor', 'pituitary']

# Prediction function
@csrf_exempt
def PredictTumor(request):
    if request.method == 'POST' and 'file' in request.FILES:
        try:
            # Convert uploaded file to a format TensorFlow can process
            image_file = request.FILES['file']
            img = Image.open(image_file)

            # Ensure the image is in RGB format
            img = img.convert('RGB')
            img = img.resize((224, 224))  # Resize image to model input size
            img_array = img_to_array(img)
            img_array = tf.expand_dims(img_array, axis=0)  # Add batch dimension
            img_array /= 255.0  # Normalize pixel values

            # Make prediction
            predictions = model_layer(img_array)
            predictions = predictions['output_layer'][0].numpy()  # Replace 'output_layer' with your model's output layer key if different

            # Prepare the response
            response = {
                CLASS_LABELS[i]: float(predictions[i]) for i in range(len(CLASS_LABELS))
            }

            return JsonResponse(response, status=200)

        except Exception as e:
            return JsonResponse({'error': f'An error occurred: {str(e)}'}, status=500)

    return JsonResponse({'error': 'Invalid request. Please use POST with an image file.'}, status=400)
