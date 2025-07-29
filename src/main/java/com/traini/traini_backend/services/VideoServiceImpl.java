package com.traini.traini_backend.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.traini.traini_backend.dto.video.UpdateVideoDto;
import com.traini.traini_backend.enums.Role;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.models.InteractionModel;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.repository.DepartmentRepository;
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

    @Autowired
    private DepartmentRepository departmentRepository;
  

    @Override
    public String uploadAndSaveVideo(MultipartFile videoFile, MultipartFile thumbnail, String title, String description, DepartmentModel department, Long duration, Authentication authentication) throws Exception {
        String email = authentication.getName();
        Optional<EmployeeModel> employeeOpt = employeeRepository.findByEmail(email);
        
        if (!employeeOpt.isPresent()) {
            throw new Exception("Usuario no encontrado");
        }
        
        EmployeeModel employee = employeeOpt.get();
        Role userRole = employee.getRole().getName();
        
        if (!userRole.equals(Role.ADMIN) && !userRole.equals(Role.SUPER_ADMIN) && !userRole.equals(Role.SUPERVISOR)) {
            throw new Exception("No tienes permisos para subir videos. Solo administradores y supervisores pueden subir videos.");
        }
        if (userRole.equals(Role.SUPERVISOR)) {
            if (!department.getId().equals(employee.getDepartment().getId())) {
                throw new Exception("No tienes permisos para subir videos a este departamento. Los supervisores solo pueden subir videos a su propio departamento.");
            }
        }
        if (!userRole.equals(Role.SUPER_ADMIN)) {
            Long tenantId = TenantContext.getCurrentTenant();
            if (tenantId != null && !department.getCompany().getId().equals(tenantId)) {
                throw new Exception("No tienes permisos para subir videos a este departamento.");
            }
        }
        
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
    public List<VideoModel> findAll(Authentication authentication) {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            Optional<EmployeeModel> employeeOpt = employeeRepository.findByEmail(authentication.getName());
            if (employeeOpt.isPresent()) {
                Role userRole = employeeOpt.get().getRole().getName();
                if (userRole.equals(Role.SUPERVISOR)) {
                    Long departmentId = employeeOpt.get().getDepartment().getId();
                    return videoRepository.findByCompanyIdAndDepartmentId(tenantId, departmentId);
                }
            }
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

    public Long countVideosByDepartment(Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        DepartmentModel department = employeeRepository.findDepartmentById(employeeId);
        return videoRepository.countByDepartment(department);
    }

    @Override
    @Transactional
    public void deleteVideo(Long videoId, Authentication authentication) throws Exception {
        String email = authentication.getName();
        Optional<EmployeeModel> employeeOpt = employeeRepository.findByEmail(email);
        
        if (!employeeOpt.isPresent()) {
            throw new Exception("Usuario no encontrado");
        }
        
        EmployeeModel employee = employeeOpt.get();
        Role userRole = employee.getRole().getName();
        
        if (!userRole.equals(Role.ADMIN) && !userRole.equals(Role.SUPER_ADMIN) && !userRole.equals(Role.SUPERVISOR)) {
            throw new Exception("No tienes permisos para eliminar videos. Solo administradores y supervisores pueden eliminar videos.");
        }
        
        Optional<VideoModel> videoOpt = videoRepository.findById(videoId);
        if (!videoOpt.isPresent()) {
            throw new Exception("Video no encontrado");
        }
        
        VideoModel video = videoOpt.get();
        
        if (userRole.equals(Role.SUPERVISOR)) {
            if (!video.getDepartment().getId().equals(employee.getDepartment().getId())) {
                throw new Exception("No tienes permisos para eliminar este video. Los supervisores solo pueden eliminar videos de su departamento.");
            }
        }
        
        if (!userRole.equals(Role.SUPER_ADMIN)) {
            Long tenantId = TenantContext.getCurrentTenant();
            if (tenantId != null && !video.getDepartment().getCompany().getId().equals(tenantId)) {
                throw new Exception("No tienes permisos para eliminar este video.");
            }
        }
        
        interactionRepository.deleteByVideoId(videoId);
        
        try {
            if (video.getVideoUrl() != null) {
                firebaseStorageService.deleteVideo(video.getVideoUrl());
            }
            if (video.getThumbnailUrl() != null) {
                firebaseStorageService.deleteThumbnail(video.getThumbnailUrl());
            }
        } catch (Exception e) {
            System.err.println("Error eliminando archivos de Firebase: " + e.getMessage());
        }
        
        videoRepository.delete(video);
    }

    @Override
    public void updateVideo(Long videoId, UpdateVideoDto updateRequest) throws Exception {
        VideoModel video = videoRepository.findById(videoId)
            .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));

        if (updateRequest.getTitle() != null) {
            video.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null) {
            video.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getDepartmentId() != null) {
            DepartmentModel department = departmentRepository.findById(updateRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + updateRequest.getDepartmentId()));
            video.setDepartment(department);
        }

        videoRepository.save(video);
    }
}