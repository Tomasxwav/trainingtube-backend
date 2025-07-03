package com.traini.traini_backend.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.enums.Department;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.models.EmployeeVideoInteractionModel;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.repository.EmployeeVideoInteractionRepository;
import com.traini.traini_backend.repository.VideoRepository;
import com.traini.traini_backend.services.interfaces.VideoService;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private VideoRepository videoRepository; 

    @Autowired
    private EmployeeVideoInteractionRepository interactionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
  

    @Override
    public String uploadAndSaveVideo(MultipartFile vildeo,MultipartFile thumbnail, String title, String description, Department category) throws Exception {
        String videoUrl = firebaseStorageService.uploadVideo(vildeo);
        String thumbnailUrl = firebaseStorageService.uploadThumbnail(thumbnail);

        // 2. Guardar metadatos en la base de datos
        VideoModel video = new VideoModel();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoUrl(videoUrl);
        video.setUploadDate(new Date());
        video.setThumbnailUrl(thumbnailUrl);
        video.setCategory(category); 

        VideoModel savedVideo = videoRepository.save(video);

        assignVideoToDepartmentEmployees(savedVideo);

        return videoUrl;
    }

     private void assignVideoToDepartmentEmployees(VideoModel video) {
        List<EmployeeModel> departmentEmployees = employeeRepository.findByDepartment(video.getCategory());
        
        departmentEmployees.forEach(employee -> {
            EmployeeVideoInteractionModel interaction = new EmployeeVideoInteractionModel();
            interaction.setEmployee(employee);
            interaction.setVideoId(video.getId());
            interaction.setPending(true);
            interaction.setWatched(false);
            interaction.setFavorite(false);
            interaction.setLastInteractionDate(new Date());
            
            interactionRepository.save(interaction);
        });
    }

    @Override
    public List<VideoModel> findAll() {
        return (List<VideoModel>) videoRepository.findAll();
    }
}