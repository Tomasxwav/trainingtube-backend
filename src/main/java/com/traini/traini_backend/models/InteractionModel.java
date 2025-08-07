package com.traini.traini_backend.models;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_video_interactions")
public class InteractionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private EmployeeModel employee;

    @Column(name = "video_id", nullable = false)
    private Long videoId;

    @Column(name = "is_favorite", nullable = false, columnDefinition = "boolean default false")
    private boolean isFavorite;

    @Column(name = "is_pending", nullable = false, columnDefinition = "boolean default false")
    private boolean isPending;

    @Column(name = "is_watched", nullable = false, columnDefinition = "boolean default false")
    private boolean isWatched;

    @Column(name = "progress", nullable = false, columnDefinition = "integer default 0")
    private int progress;

    @Column(name = "last_interaction_date", nullable = false)
    private Date lastInteractionDate;

    @Column(name = "finalized_date")
    private Date finalizedDate;

    // Getters, Setters, Constructors...
    public InteractionModel() {
    }
    
    public InteractionModel(EmployeeModel employee, Long videoId, boolean isFavorite, boolean isPending, boolean isWatched, Date lastInteractionDate) {
        this.employee = employee;
        this.videoId = videoId;
        this.isFavorite = isFavorite;
        this.isPending = isPending;
        this.isWatched = isWatched;
        this.progress = 0;
        this.lastInteractionDate = lastInteractionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeModel getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeModel employee) {
        this.employee = employee;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean isPending) {
        this.isPending = isPending;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean isWatched) {
        this.isWatched = isWatched;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Date getLastInteractionDate() {
        return lastInteractionDate;
    }

    public void setLastInteractionDate(Date lastInteractionDate) {
        this.lastInteractionDate = lastInteractionDate;
    }

    public Date getFinalizedDate() {
        return finalizedDate;
    }

    public void setFinalizedDate(Date finalizedDate) {
        this.finalizedDate = finalizedDate;
    }

}