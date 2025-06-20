package com.traini.traini_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traini.traini_backend.models.EmployeeVideoInteractionModel;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.services.EmployeeVideoInteractionService;

@RestController
@RequestMapping("/interactions/videos")
public class EmployeeVideoInteractionController {

    
    @Autowired
    private EmployeeVideoInteractionService service;

    public EmployeeVideoInteractionController(EmployeeVideoInteractionService service) {
        this.service = service;
    }

    @GetMapping("/{employeeId}/pending")
    public ResponseEntity<List<VideoModel>> getPendingVideos(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getPendingVideosByEmployee(employeeId));
    }

    @GetMapping("/{employeeId}/favorites")
    public ResponseEntity<List<EmployeeVideoInteractionModel>> getFavoriteVideos(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getFavoriteVideosByEmployee(employeeId));
    }
}