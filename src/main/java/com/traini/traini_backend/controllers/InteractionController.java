package com.traini.traini_backend.controllers;

import java.util.List;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traini.traini_backend.dto.UpdateInteractionDto;
import com.traini.traini_backend.models.InteractionModel;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.services.InteractionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/interactions")
public class InteractionController {

    
    @Autowired
    private InteractionService service;

    public InteractionController(InteractionService service) {
        this.service = service;
    }

    @GetMapping() // Endpoint to get all interactions
    public ResponseEntity<?> getAllInteractions(Authentication authentication) {
        return ResponseEntity.ok(service.getInteractionsByEmployee(authentication));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<VideoModel>> getPendingVideos(Authentication authentication) {
        return ResponseEntity.ok(service.getPendingVideos(authentication));
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<VideoModel>> getFavoriteVideos(Authentication authentication) {
        return ResponseEntity.ok(service.getFavoriteVideos(authentication));
        /* return ResponseEntity.ok(service.getFavoriteVideos()); */
    }

    @GetMapping("/likes")
    public ResponseEntity<String> getLikedVideos(Authentication authentication) {
        return ResponseEntity.ok("TODO");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVideoEmployeeInteractions(@PathVariable Long id, Authentication authentication) {
        InteractionModel interaction = service.getVideoEmployeeInteractions(id, authentication);
        return ResponseEntity.ok(interaction);
    }

    @PostMapping("/favorites/{id}")
    public ResponseEntity<String> toggleFavoriteVideo(@PathVariable Long id, Authentication authentication) {
        service.toggleFavorite(id, authentication);
        return ResponseEntity.ok("Video favorite status updated successfully");
    }

    @PostMapping("/likes/{id}")
    public ResponseEntity<String> toggleLikeVideo(@PathVariable Long id, Authentication authentication) {
        service.toggleLike(id, authentication);
        return ResponseEntity.ok("TODO");
    }

    @PostMapping("/pending/{id}")
    public ResponseEntity<String> togglePendingVideo(@PathVariable Long id, Authentication authentication) {
        service.togglePending(id, authentication);
        return ResponseEntity.ok("Video pending status updated successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVideoEmployeeInteraction(@PathVariable Long id, @RequestBody UpdateInteractionDto updateDto, Authentication authentication) {
        service.updateVideoEmployeeInteraction(id, updateDto, authentication);
        return ResponseEntity.ok("Video employee interaction updated successfully");
    }
    
}