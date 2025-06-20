package com.traini.traini_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.traini.traini_backend.enums.Department;
import com.traini.traini_backend.models.EmployeeVideoInteractionModel;
import com.traini.traini_backend.models.VideoModel;

public interface EmployeeVideoInteractionRepository 
    extends JpaRepository<EmployeeVideoInteractionModel, Long> {


     List<EmployeeVideoInteractionModel> findByEmployeeId(Long employeeId);
    
    boolean existsByEmployeeIdAndVideoIdAndIsFavorite(Long employeeId, Long videoId, boolean isFavorite);

    // Buscar una interacción específica por empleado y video
    EmployeeVideoInteractionModel findByEmployeeIdAndVideoId(Long employeeId, Long videoId);

    // Obtener todos los videos PENDIENTES de un empleado
    List<EmployeeVideoInteractionModel> findByEmployeeIdAndIsPending(Long employeeId, boolean isPending);

    // Obtener todos los videos FAVORITOS de un empleado
    List<EmployeeVideoInteractionModel> findByEmployeeIdAndIsFavorite(Long employeeId, boolean isFavorite);

    // Otra forma de obtener todos los videos PENDIENTES de un empleado 
    @Query("SELECT v FROM VideoModel v WHERE v.category = :department AND EXISTS (SELECT 1 FROM EmployeeVideoInteractionModel i WHERE i.videoId = v.id AND i.employee.id = :employeeId AND i.pending = true)")
    List<VideoModel> findPendingVideosByEmployee(@Param("department") Department department, @Param("employeeId") Long employeeId);
}