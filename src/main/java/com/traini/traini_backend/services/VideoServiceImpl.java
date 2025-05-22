package com.traini.traini_backend.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.enums.Category;
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
    public String uploadAndSaveVideo(MultipartFile vildeo,MultipartFile thumbnail, String title, String description, Category category) throws Exception {
        String videoUrl = firebaseStorageService.uploadVideo(vildeo);
        String thumbnailUrl = firebaseStorageService.uploadThumbnail(thumbnail);

        // 2. Guardar metadatos en la base de datos
        VideoModel video = new VideoModel();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoUrl(videoUrl);
        video.setUploadDate(new Date());
        video.setUrlThumbnail(thumbnailUrl);
        video.setCategory(category); 

        videoRepository.save(video);

        return videoUrl;
    }

    @Override
    public List<VideoModel> findAll() {
        return (List<VideoModel>) videoRepository.findAll();
    }
}