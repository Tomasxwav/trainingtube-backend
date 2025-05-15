package com.traini.traini_backend.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.pathtemplate.ValidationException;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.services.interfaces.VideoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/videos")
public class VideosController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public ResponseEntity<List<VideoModel>> GetVideos() {
        List<VideoModel> videos = videoService.findAll();
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoModel> GetVideo(@PathVariable String id) {
        VideoModel video = videoService.findById(Long.parseLong(id));
        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> uploadVideo(@RequestBody VideoModel video) {
       try {
            VideoModel savedVideo = videoService.save(video);
            return ResponseEntity.ok(savedVideo);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }
    
}
