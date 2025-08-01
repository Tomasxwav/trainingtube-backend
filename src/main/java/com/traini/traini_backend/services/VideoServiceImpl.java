package com.traini.traini_backend.services;

import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.models.InteractionModel;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.repository.InteractionRepository;
import com.traini.traini_backend.repository.VideoRepository;
import com.traini.traini_backend.services.interfaces.VideoService;
import com.traini.traini_backend.config.TenantContext;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private VideoRepository videoRepository; 

    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
  

    @Override
    public String uploadAndSaveVideo(MultipartFile videoFile, MultipartFile thumbnail, String title, String description, DepartmentModel department, Long duration) throws Exception {
        String videoUrl = firebaseStorageService.uploadVideo(videoFile);
        String thumbnailUrl = firebaseStorageService.uploadThumbnail(thumbnail);

        // 2. Guardar metadatos en la base de datos
        VideoModel video = new VideoModel();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoUrl(videoUrl);
        video.setUploadDate(new Date());
        video.setThumbnailUrl(thumbnailUrl);
        video.setDepartment(department);
        video.setDuration(duration);
        video.setViews((long) 0);

        VideoModel savedVideo = videoRepository.save(video);

        assignVideoToDepartmentEmployees(savedVideo);

        return videoUrl;
    }

     private void assignVideoToDepartmentEmployees(VideoModel video) {
        List<EmployeeModel> departmentEmployees = employeeRepository.findByDepartment(video.getDepartment());

        System.out.println("Assigning video to department employees" + departmentEmployees.size());
        departmentEmployees.forEach(employee -> {
            System.out.println("Assigning video to employee " + employee.getEmail());
            InteractionModel interaction = new InteractionModel();
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
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            return videoRepository.findByCompanyId(tenantId);
        }
        return (List<VideoModel>) videoRepository.findAll();
    }

    public List<VideoModel> findVideoByDepartment(Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        DepartmentModel department = employeeRepository.findDepartmentById(employeeId);
        return videoRepository.findByDepartment(department);
    }
}