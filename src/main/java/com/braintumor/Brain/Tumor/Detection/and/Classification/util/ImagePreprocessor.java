package com.braintumor.Brain.Tumor.Detection.and.Classification.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ImagePreprocessor {

    public static byte[] preprocessImage(MultipartFile file) throws IOException {
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

        // Ensure the image is not null
        if (img == null) {
            throw new IOException("Invalid image format or corrupted file.");
        }

        // Convert grayscale or binary images to RGB
        if (img.getColorModel().getNumComponents() == 1) {
            BufferedImage rgbImage = new BufferedImage(224, 224, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = rgbImage.createGraphics();
            g.drawImage(img, 0, 0, 224, 224, null);
            g.dispose();
            img = rgbImage;
        } else if (img.getColorModel().getNumComponents() == 3) {
            // Image already in RGB format, resize it to 224x224
            BufferedImage resizedImg = new BufferedImage(224, 224, BufferedImage.TYPE_INT_RGB);
            resizedImg.getGraphics().drawImage(img, 0, 0, 224, 224, null);
            img = resizedImg;
        }

        // Extract RGB data from the image
        byte[] imageData = new byte[224 * 224 * 3];
        int index = 0;
        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                int rgb = img.getRGB(x, y);
                imageData[index++] = (byte) ((rgb >> 16) & 0xFF); // Red
                imageData[index++] = (byte) ((rgb >> 8) & 0xFF);  // Green
                imageData[index++] = (byte) (rgb & 0xFF);         // Blue
            }
        }

        return imageData;
    }
}
