package com.traini.traini_backend.services.interfaces;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.traini.traini_backend.models.CommentsModel;

public interface CommentsService {
    List<CommentsModel> findByVideoId(Long videoId);
    
    List<CommentsModel> findByVideoIdAndIsDeletedFalse(Long videoId);
    
    List<CommentsModel> findByVideoIdAndParentCommentIdIsNullAndIsDeletedFalse(Long videoId);
    
    List<CommentsModel> findByParentCommentIdAndIsDeletedFalse(Long parentCommentId);
    
    List<CommentsModel> findByVideoIdAndIsDeletedFalseOrderByCreatedAtAsc(Long videoId);
    
    List<CommentsModel> findByVideoIdAndIsDeletedFalseOrderByCreatedAtDesc(Long videoId);
    
    Long countByVideoIdAndIsDeletedFalse(Long videoId);
    
    List<CommentsModel> findByEmployeeId(Long employeeId);
    
    List<CommentsModel> findByEmployeeComments(Authentication authentication);
    
    CommentsModel save(CommentsModel comment, Authentication authentication);
    
    void delete(Long id, Authentication authentication);

    List<CommentsModel> findAll(Authentication authentication);
}
