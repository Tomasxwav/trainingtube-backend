package com.traini.traini_backend.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.repository.VideoRepository;
import com.traini.traini_backend.services.interfaces.VideoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FirebaseService firebaseService;


    @Override
    public List<VideoModel> findAll() {
        return (List<VideoModel>) videoRepository.findAll();
    }

    @Override
    public VideoModel findById(Long id) {
        return videoRepository.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("The video with id %s not found.", id)) );
    }


    @Override
    public VideoModel save(VideoModel video)  {
        videoRepository.findByTitle(video.getTitle())
            .ifPresent( videoDB -> {
                throw new ValidationException(String.format("Video with title %s already exists", videoDB.getTitle()));
            });
        
        if( video.getFile() == null ) throw new ValidationException("Video file is required");

        if( video.getTitle() == null ) throw new ValidationException("Video title is required");

        if( video.getDescription() == null ) throw new ValidationException("Video description is required");

        String title = video.getTitle();

        video.setTitle(title.replaceAll("[^a-zA-Z0-9]", ""));

        try {
            firebaseService.uploadVideo(video.getFile(), video.getTitle());
            return videoRepository.save(video);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload video file", e);
        }  
    }

    @Override
    public VideoModel update(Long id, VideoModel video) {
        VideoModel videoFound = findById(id);

        if( video.getTitle() != null ) videoFound.setTitle(video.getTitle());
        if( video.getDescription() != null ) videoFound.setDescription(video.getDescription());
        if( video.getUploadDate() != null ) videoFound.setUploadDate(video.getUploadDate());


        return videoRepository.save(videoFound);

    }

    @Override
    public VideoModel delete(Long id) {
        VideoModel videoFound = findById(id);
        videoRepository.deleteById(id);
        return videoFound;
    }
    


}