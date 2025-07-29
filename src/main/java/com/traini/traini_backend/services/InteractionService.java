package com.traini.traini_backend.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import com.traini.traini_backend.models.InteractionModel;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.repository.InteractionRepository;
import com.traini.traini_backend.repository.VideoRepository;
import com.traini.traini_backend.dto.UpdateInteractionDto;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private VideoRepository videoRepository;

    // Guardar o actualizar una interacción
    public InteractionModel saveInteraction(InteractionModel interaction) {
        interaction.setLastInteractionDate(new Date());
        return repository.save(interaction);
    }

    // Obtener interacciones de un empleado
    public List<InteractionModel> getInteractionsByEmployee(Long employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    // Marcar/desmarcar como favorito
    public InteractionModel toggleFavorite(Long employeeId, Long videoId, boolean isFavorite) {
        InteractionModel interaction = repository.findByEmployeeIdAndVideoId(employeeId, videoId);
        if (interaction == null) {
            interaction = new InteractionModel();
            interaction.setId(employeeId);
            interaction.setVideoId(videoId);
        }
        interaction.setFavorite(isFavorite);
        return saveInteraction(interaction);
    }

    // Obtener videos pendientes de un empleado
    public List<VideoModel> getPendingVideos(Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();

        DepartmentModel department = employeeRepository.findDepartmentById(employeeId);
        return repository.findPendingVideosByEmployee(department.getId(), employeeId);
    }

    // Obtener videos favoritos del empleado actual
    public List<VideoModel> getFavoriteVideos(Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        return repository.findFavoritesVideosByEmployee(employeeId);
        
    }

    // Marcar/desmarcar como favorito
    public InteractionModel toggleFavorite(Long id, Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        InteractionModel interaction = repository.findByEmployeeIdAndVideoId(employeeId, id);
        if (interaction == null) {
            interaction = new InteractionModel();
            interaction.setId(employeeId);
            interaction.setVideoId(id);
        }
        interaction.setFavorite(!interaction.isFavorite());
        return repository.save(interaction);
    }

    // Marcar/desmarcar como pendiente
    public InteractionModel togglePending(Long id, Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        InteractionModel interaction = repository.findByEmployeeIdAndVideoId(employeeId, id);
        if (interaction == null) {
            interaction = new InteractionModel();
            interaction.setId(employeeId);
            interaction.setVideoId(id);
        }
        interaction.setPending(false);
        return repository.save(interaction);
    }

    // Marcar/desmarcar como favorito
    public InteractionModel toggleLike(Long id, Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        InteractionModel interaction = repository.findByEmployeeIdAndVideoId(employeeId, id);
        if (interaction == null) {
            interaction = new InteractionModel();
            interaction.setId(employeeId);
            interaction.setVideoId(id);
        }
        interaction.setWatched(!interaction.isWatched());
        return repository.save(interaction);
    }

    // Obtener interacciones de un empleado
    public List<InteractionModel> getInteractionsByEmployee(Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        return repository.findByEmployeeId(employeeId);
    }

    // Obtener las interacciones del empleado para un video específico
    public InteractionModel getVideoEmployeeInteractions(Long id, Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        return repository.findByEmployeeIdAndVideoId(employeeId, id);

    }
    
    // Actualizar interación del empleado con un video específico
   public InteractionModel updateVideoEmployeeInteraction(Long id, UpdateInteractionDto updateDto, Authentication authentication) {
       String email = authentication.getName();
       Long employeeId = employeeRepository.findByEmail(email).get().getId();
       InteractionModel existingInteraction = repository.findByEmployeeIdAndVideoId(employeeId, id);
       
       if (existingInteraction == null) {
           throw new RuntimeException("No existe una interacción entre el empleado y el video con ID: " + id);
       }
       
       if (updateDto.getProgress() != null) {
           existingInteraction.setProgress(updateDto.getProgress());
       }
       
       if (updateDto.getIsFavorite() != null) {
           existingInteraction.setFavorite(updateDto.getIsFavorite());
       }
       
       if (updateDto.getIsPending() != null) {
           existingInteraction.setPending(updateDto.getIsPending());
            if (updateDto.getIsPending() == false) {
               VideoModel video = videoRepository.findById(existingInteraction.getVideoId()).get();
               video.setViews(video.getViews() + 1);
               videoRepository.save(video);
           }
       }
       
       if (updateDto.getIsWatched() != null) {
           existingInteraction.setWatched(updateDto.getIsWatched());
       }
       
       if (updateDto.getLastInteractionDate() != null) {
           existingInteraction.setLastInteractionDate(updateDto.getLastInteractionDate());
       } else {
           existingInteraction.setLastInteractionDate(new Date());
       }

       return repository.save(existingInteraction);
   }
}