import h5py
import shutil

# Paths to the model files
model_path = 'densenet_brain_tumor.h5'
fixed_model_path = 'densenet_brain_tumor_fixed.h5'

# Copy the original file to preserve it
shutil.copyfile(model_path, fixed_model_path)

# Open the copied HDF5 file for modification
with h5py.File(fixed_model_path, 'r+') as f:
    layer_weights = f['model_weights']
    layer_names = list(layer_weights.keys())
    
    for layer_name in layer_names:
        if '/' in layer_name:  # Check for slashes in the name
            fixed_name = layer_name.replace('/', '_')  # Replace '/' with '_'
            layer_weights.move(layer_name, fixed_name)  # Rename the layer

print(f"Layer names fixed and saved to {fixed_model_path}")
