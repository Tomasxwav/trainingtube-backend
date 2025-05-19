package com.traini.traini_backend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.repository.VideoRepository;
import com.traini.traini_backend.services.interfaces.VideoService;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private VideoRepository videoRepository; 

    @Override
    public String uploadAndSaveVideo(MultipartFile file, String title, String description) throws Exception {
        // 1. Subir el video a Firebase (delegado a FirebaseStorageService)
        String videoUrl = firebaseStorageService.uploadVideo(file);

        // 2. Guardar metadatos en la base de datos
        VideoModel video = new VideoModel();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoUrl(videoUrl);
        video.setUploadDate(new Date());

        videoRepository.save(video);

        return videoUrl;
    }
}