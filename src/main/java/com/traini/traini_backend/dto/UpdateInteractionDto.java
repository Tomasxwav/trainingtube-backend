package com.traini.traini_backend.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateInteractionDto {

    private Integer progress;
    private Boolean isFavorite;
    private Boolean isPending;
    private Boolean isWatched;
    private Date lastInteractionDate;

    public UpdateInteractionDto() {
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Boolean getIsPending() {
        return isPending;
    }

    public void setIsPending(Boolean isPending) {
        this.isPending = isPending;
    }

    public Boolean getIsWatched() {
        return isWatched;
    }

    public void setIsWatched(Boolean isWatched) {
        this.isWatched = isWatched;
    }

    public Date getLastInteractionDate() {
        return lastInteractionDate;
    }

    public void setLastInteractionDate(Date lastInteractionDate) {
        this.lastInteractionDate = lastInteractionDate;
    }
}
