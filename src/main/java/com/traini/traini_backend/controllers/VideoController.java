package com.traini.traini_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.services.VideoServiceImpl;


@RestController
@RequestMapping("/api/videos")
public class VideoController {
    
    private final VideoServiceImpl videoService;

    public VideoController(VideoServiceImpl videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<?> uploadVideo(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("video") MultipartFile videoFile,
            @RequestParam("thumbnail") MultipartFile thumbnailFile) {
        VideoModel video = videoService.saveVideo(titulo, descripcion, videoFile, thumbnailFile);
        return ResponseEntity.ok(video);
    }
}