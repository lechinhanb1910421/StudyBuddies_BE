package com.everett.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PostDTO {
    private Long userId;
    private String content;
    private String audienceMode;
    private Long topicId;
    private Long majorId;

    public PostDTO() {
    }

    public PostDTO(Long userId, String content, String audienceMode, Long topicId, Long majorId) {
        this.userId = userId;
        this.content = content;
        this.audienceMode = audienceMode;
        this.topicId = topicId;
        this.majorId = majorId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAudienceMode() {
        return audienceMode;
    }

    public void setAudienceMode(String audienceMode) {
        this.audienceMode = audienceMode;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getMajorId() {
        return majorId;
    }

    public void setMajorId(Long majorId) {
        this.majorId = majorId;
    }

    @JsonIgnore
    public boolean isMissingKeys() {
        if (this.userId == null) {
            return true;
        }
        if (this.content == null) {
            return true;
        }
        if (this.audienceMode == null) {
            return true;
        }
        if (this.topicId == null) {
            return true;
        }
        if (this.majorId == null) {
            return true;
        }
        return false;
    }

}
