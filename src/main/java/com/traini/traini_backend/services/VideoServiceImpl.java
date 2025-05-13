package com.traini.traini_backend.services;

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

    @Override
    public List<VideoModel> findAll() {
        return (List<VideoModel>) videoRepository.findAll();
    }

    @Override
    public VideoModel findById(Long id) {
        return videoRepository.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("The video with id %s not found.", id)) );
    }

    @Override
    public VideoModel save(VideoModel video) {

        videoRepository.findByTitle(video.getTitle())
            .ifPresent( videoDB -> {
                throw new ValidationException(String.format("Video with title %s already exists", videoDB.getTitle()));
            });

        return videoRepository.save(video);
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