package com.traini.traini_backend.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    String uploadAndSaveVideo(MultipartFile file, String title, String description) throws Exception;
}