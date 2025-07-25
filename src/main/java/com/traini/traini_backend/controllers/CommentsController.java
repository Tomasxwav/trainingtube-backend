package com.traini.traini_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traini.traini_backend.models.CommentsModel;
import com.traini.traini_backend.services.interfaces.CommentsService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/comments")
public class CommentsController {
    
    @Autowired
    private CommentsService commentsService;

    @GetMapping("/{videoId}")
    public ResponseEntity<?> getComments(@PathVariable Long videoId, Authentication authentication) {
        try {
            return ResponseEntity.ok(commentsService.findByVideoIdAndParentCommentIdIsNullAndIsDeletedFalse(videoId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid video ID: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching comments: " + e.getMessage());
        }
    }
    
    @PostMapping("/{videoId}")
    public ResponseEntity<?> postComment(@PathVariable Long videoId, @Valid @RequestBody CommentsModel comment, Authentication authentication) {
        try {
            comment.setVideoId(videoId);
            CommentsModel savedComment = commentsService.save(comment, authentication);
            return ResponseEntity.ok(savedComment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while saving the comment: " + e.getMessage());
        }
    }

    @GetMapping("/my-comments")
    public ResponseEntity<?> getMyComments(Authentication authentication) {
        try {
            return ResponseEntity.ok(commentsService.findByEmployeeComments(authentication));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid authentication: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching comments: " + e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        try {
            commentsService.delete(commentId, authentication);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body("Access denied: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while deleting the comment: " + e.getMessage());
        }
    }
}
