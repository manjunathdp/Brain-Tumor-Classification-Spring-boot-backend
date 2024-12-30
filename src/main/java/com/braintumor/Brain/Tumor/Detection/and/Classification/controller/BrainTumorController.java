package com.braintumor.Brain.Tumor.Detection.and.Classification.controller;

import com.braintumor.Brain.Tumor.Detection.and.Classification.service.BrainTumorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class BrainTumorController {

    @Autowired
    private BrainTumorService brainTumorService;

    @PostMapping("/predict")
    public ResponseEntity<?> predictTumor(@RequestParam("image") MultipartFile file) {
        try {
            return ResponseEntity.ok(brainTumorService.predict(file));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing image: " + e.getMessage());
        }
    }
}
