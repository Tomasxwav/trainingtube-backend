package com.traini.traini_backend.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.enums.Category;
import com.traini.traini_backend.models.VideoModel;

public interface VideoService {
    String uploadAndSaveVideo(MultipartFile video, MultipartFile thumbnail , String title, String description, Category category) throws Exception; 
    List<VideoModel> findAll();
}