package com.traini.traini_backend.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.services.VideoServiceImpl;
import com.traini.traini_backend.services.interfaces.DepartmentService;

@RestController
@RequestMapping("/videos")
public class VideoController {
    @Autowired
    private VideoServiceImpl videoService; 
    
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/admin")
    public ResponseEntity<String> uploadVideo(
            @RequestParam("video") MultipartFile video,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Long department_id,
            @RequestParam Integer duration
            ) {
        
        try {
            DepartmentModel department = departmentService.findById(department_id);
            String videoUrl = videoService.uploadAndSaveVideo(video, thumbnail, title, description, department, duration);
            return ResponseEntity.ok("Video subido exitosamente. URL: " + videoUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAllVideos() {
        try {
            return ResponseEntity.ok(videoService.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/department")
    public ResponseEntity<?> getDepartmentVideos(Authentication authentication) {
        try {
            return ResponseEntity.ok(videoService.findVideoByDepartment(authentication));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}