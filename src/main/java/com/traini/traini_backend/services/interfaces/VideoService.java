package com.traini.traini_backend.services.interfaces;

import java.util.List;

import com.traini.traini_backend.models.VideoModel;

public interface VideoService {
        
    List<VideoModel> findAll();
    VideoModel findById(Long id);
    /* VideoModel save(VideoModel video); */
    VideoModel update(Long id, VideoModel video);
    VideoModel delete(Long id);
    
}
