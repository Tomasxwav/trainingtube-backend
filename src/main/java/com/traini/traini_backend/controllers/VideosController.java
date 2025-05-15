package com.traini.traini_backend.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.repository.VideoRepository;
import com.traini.traini_backend.services.FirebaseService;

@RestController
@RequestMapping("/videos")
public class VideosController {

    private final VideoRepository videoRepository;
    private final FirebaseService firebaseService;

    public VideosController(VideoRepository videoRepository, FirebaseService firebaseService) {
        this.videoRepository = videoRepository;
        this.firebaseService = firebaseService;
    }

    @GetMapping
    public ResponseEntity<List<VideoModel>> getVideos() {
        List<VideoModel> videos = (List<VideoModel>) videoRepository.findAll();
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoModel> getVideo(@PathVariable String id) {
        return videoRepository.findById(Long.parseLong(id))
                .map(video -> new ResponseEntity<>(video, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<VideoModel> uploadVideo(
            @RequestParam("video") MultipartFile file,
            @RequestParam String title,
            @RequestParam String description) throws IOException {
        
        String firebaseUrl = firebaseService.uploadFile(file, "videos");
        
        VideoModel video = new VideoModel();
        video.setTitle(title);
        video.setDescription(description);
        video.setFirebaseUrl(firebaseUrl);
        video.setFirebasePath("tutoriales/" + video.getTitle());
        video.setFileName(video.getTitle());
        video.setFileType(file.getContentType());
        video.setFileSize(file.getSize());
        
        VideoModel savedVideo = videoRepository.save(video);
        
        return ResponseEntity.ok(savedVideo);
    }
}