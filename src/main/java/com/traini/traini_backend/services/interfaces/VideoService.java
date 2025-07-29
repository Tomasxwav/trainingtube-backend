package com.traini.traini_backend.services.interfaces;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.dto.video.UpdateVideoDto;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.VideoModel;

public interface VideoService {
    String uploadAndSaveVideo(MultipartFile video, MultipartFile thumbnail , String title, String description, DepartmentModel department, Long duration, Authentication authentication) throws Exception; 
    List<VideoModel> findAll(Authentication authentication);
    
    void deleteVideo(Long videoId, Authentication authentication) throws Exception;
    void updateVideo(Long videoId, UpdateVideoDto updateRequest, Authentication authentication) throws Exception;

}