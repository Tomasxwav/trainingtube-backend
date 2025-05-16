package com.traini.traini_backend.services;

import com.traini.traini_backend.models.VideoModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.repository.VideoRepository;

@Service
public class VideoServiceImpl {

    private VideoRepository videoRepository;
    private FirebaseStorageService firebaseService;

    public VideoServiceImpl(VideoRepository videoRepository, FirebaseStorageService firebaseService) {
        this.videoRepository = videoRepository;
        this.firebaseService = firebaseService;
    }

    public VideoModel saveVideo(String titulo, String descripcion, MultipartFile videoFile, MultipartFile thumbnailFile) {
        try {
            // Subir archivos a Firebase
            String videoUrl = firebaseService.uploadVideo(videoFile);
            String thumbnailUrl = firebaseService.uploadThumbnail(thumbnailFile);

            // Guardar metadatos en PostgreSQL
            VideoModel video = new VideoModel();
            video.setTitulo(titulo);
            video.setDescripcion(descripcion);
            video.setUrlVideo(videoUrl);
            video.setUrlThumbnail(thumbnailUrl);

            return videoRepository.save(video);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el video: " + e.getMessage());
        }
    }
}
