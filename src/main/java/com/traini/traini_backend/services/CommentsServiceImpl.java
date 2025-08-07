package com.traini.traini_backend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traini.traini_backend.models.CommentsModel;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.repository.CommentsRepository;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.services.interfaces.CommentsService;

@Service
@Transactional
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentsModel> findByVideoId(Long videoId) {
        if (videoId == null) {
            throw new IllegalArgumentException("Video ID cannot be null");
        }
        return commentsRepository.findByVideoId(videoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsModel> findByVideoIdAndIsDeletedFalse(Long videoId) {
        if (videoId == null) {
            throw new IllegalArgumentException("Video ID cannot be null");
        }
        return commentsRepository.findByVideoIdAndIsDeletedFalse(videoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsModel> findByVideoIdAndParentCommentIdIsNullAndIsDeletedFalse(Long videoId) {
        if (videoId == null) {
            throw new IllegalArgumentException("Video ID cannot be null");
        }
        return commentsRepository.findByVideoIdAndParentCommentIdIsNullAndIsDeletedFalse(videoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsModel> findByParentCommentIdAndIsDeletedFalse(Long parentCommentId) {
        if (parentCommentId == null) {
            throw new IllegalArgumentException("Parent comment ID cannot be null");
        }
        return commentsRepository.findByParentCommentIdAndIsDeletedFalse(parentCommentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsModel> findByVideoIdAndIsDeletedFalseOrderByCreatedAtAsc(Long videoId) {
        if (videoId == null) {
            throw new IllegalArgumentException("Video ID cannot be null");
        }
        return commentsRepository.findByVideoIdAndIsDeletedFalseOrderByCreatedAtAsc(videoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsModel> findByVideoIdAndIsDeletedFalseOrderByCreatedAtDesc(Long videoId) {
        if (videoId == null) {
            throw new IllegalArgumentException("Video ID cannot be null");
        }
        return commentsRepository.findByVideoIdAndIsDeletedFalseOrderByCreatedAtDesc(videoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByVideoIdAndIsDeletedFalse(Long videoId) {
        if (videoId == null) {
            throw new IllegalArgumentException("Video ID cannot be null");
        }
        return commentsRepository.countByVideoIdAndIsDeletedFalse(videoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsModel> findByEmployeeId(Long employeeId) {
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        return commentsRepository.findByEmployeeId(employeeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsModel> findByEmployeeComments(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }
        String email = authentication.getName();
        EmployeeModel employee = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
        return commentsRepository.findByEmployeeId(employee.getId());
    }

    @Override
    public CommentsModel save(CommentsModel comment, Authentication authentication) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }
        
        String email = authentication.getName();
        EmployeeModel employee = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
        
        comment.setEmployeeId(employee.getId());
        comment.setEmployeeName(employee.getName());
        comment.setEmployeeDepartment(employee.getDepartment().getName());

        if (comment.getId() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        } else {
            comment.setUpdatedAt(LocalDateTime.now());
        }
        
        return commentsRepository.save(comment);
    }

    @Override
    public void delete(Long id, Authentication authentication) {
        if (id == null) {
            throw new IllegalArgumentException("Comment ID cannot be null");
        }
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }
        
        String email = authentication.getName();
        EmployeeModel employee = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
        
        CommentsModel comment = commentsRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        
        if (!comment.getEmployeeId().equals(employee.getId())) {
            throw new RuntimeException("Employee can only delete their own comments");
        }
        
        comment.setIsDeleted(true);
        comment.setUpdatedAt(LocalDateTime.now());
        commentsRepository.save(comment);
    }


    @Override
    public List<CommentsModel> findAll(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }
        return (List<CommentsModel>) commentsRepository.findAll();
    }
    
}
