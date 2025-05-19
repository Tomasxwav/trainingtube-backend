package com.traini.traini_backend.controllers;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.services.FirebaseStorageService;
import com.traini.traini_backend.services.VideoServiceImpl;


@RestController
@RequestMapping("/videos")
public class VideoController {
    @Autowired
    private VideoServiceImpl videoService; 

    @PostMapping
    public ResponseEntity<String> uploadVideo(
            @RequestParam("video") MultipartFile file,
            @RequestParam String title,
            @RequestParam String description) {
        
        try {
            String videoUrl = videoService.uploadAndSaveVideo(file, title, description);
            return ResponseEntity.ok("Video subido exitosamente. URL: " + videoUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}