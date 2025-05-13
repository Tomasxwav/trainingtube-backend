package com.traini.traini_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.services.interfaces.VideoService;


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
}
