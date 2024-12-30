package com.braintumor.Brain.Tumor.Detection.and.Classification.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ImagePreprocessor {

    public static byte[] preprocessImage(MultipartFile file) throws IOException {
        // Convert MultipartFile to BufferedImage
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

        // Resize image to 224x224
        BufferedImage resizedImg = new BufferedImage(224, 224, BufferedImage.TYPE_INT_RGB);
        resizedImg.getGraphics().drawImage(img, 0, 0, 224, 224, null);

        // Normalize pixel values to [0, 1] and flatten the image into a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImg, Objects.requireNonNull(file.getContentType()).endsWith("png") ? "png" : "jpg", baos);

        return baos.toByteArray();
    }
}