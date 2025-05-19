package com.traini.traini_backend.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.models.VideoModel;

public interface VideoService {
    String uploadAndSaveVideo(MultipartFile file, String title, String description) throws Exception;
    List<VideoModel> findAll();
}