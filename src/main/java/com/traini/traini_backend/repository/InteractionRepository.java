package com.traini.traini_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.traini.traini_backend.enums.Department;
import com.traini.traini_backend.models.InteractionModel;
import com.traini.traini_backend.models.VideoModel;

public interface InteractionRepository 
    extends JpaRepository<InteractionModel, Long> {


     List<InteractionModel> findByEmployeeId(Long employeeId);
    
    boolean existsByEmployeeIdAndVideoIdAndIsFavorite(Long employeeId, Long videoId, boolean isFavorite);

    // Buscar una interacción específica por empleado y video
    InteractionModel findByEmployeeIdAndVideoId(Long employeeId, Long videoId);

    // Obtener todos los videos PENDIENTES de un empleado
    List<InteractionModel> findByEmployeeIdAndIsPending(Long employeeId, boolean isPending);

    // Obtener todos los videos FAVORITOS de un empleado
    List<InteractionModel> findByEmployeeIdAndIsFavorite(Long employeeId, boolean isFavorite);

    // Otra forma de obtener todos los videos PENDIENTES de un empleado 
    @Query("SELECT v FROM VideoModel v WHERE v.department = :department AND EXISTS (SELECT 1 FROM InteractionModel i WHERE i.videoId = v.id AND i.employee.id = :employeeId AND i.isPending = true)")
    List<VideoModel> findPendingVideosByEmployee(@Param("department") Department department, @Param("employeeId") Long employeeId);

    @Query("SELECT v FROM VideoModel v WHERE EXISTS (SELECT 1 FROM InteractionModel i WHERE i.videoId = v.id AND i.employee.id = :employeeId AND i.isFavorite = true)")
    List<VideoModel> findFavoritesVideosByEmployee(@Param("employeeId") Long employeeId);
}