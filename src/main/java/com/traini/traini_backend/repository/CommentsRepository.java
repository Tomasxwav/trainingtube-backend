package com.traini.traini_backend.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.traini.traini_backend.models.CommentsModel;

public interface CommentsRepository extends CrudRepository<CommentsModel, Long> {
    
    List<CommentsModel> findByVideoId(Long videoId);
    
    List<CommentsModel> findByVideoIdAndIsDeletedFalse(Long videoId);
    
    List<CommentsModel> findByVideoIdAndParentCommentIdIsNullAndIsDeletedFalse(Long videoId);
    
    List<CommentsModel> findByParentCommentIdAndIsDeletedFalse(Long parentCommentId);
    
    List<CommentsModel> findByVideoIdAndIsDeletedFalseOrderByCreatedAtAsc(Long videoId);
    
    List<CommentsModel> findByVideoIdAndIsDeletedFalseOrderByCreatedAtDesc(Long videoId);
    
    Long countByVideoIdAndIsDeletedFalse(Long videoId);

    List<CommentsModel> findByEmployeeId(Long employeeId);    
}
