package com.everett.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PostReceiveDTO {
    private String content;
    private String audienceMode;
    private Long topicId;
    private Long majorId;
    private String imageUrl;

    public PostReceiveDTO() {
    }

    public PostReceiveDTO(String content, String audienceMode, Long topicId, Long majorId, String imageUrl) {
        this.content = content;
        this.audienceMode = audienceMode;
        this.topicId = topicId;
        this.majorId = majorId;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonIgnore
    public boolean isUpdatable() {
        if (this.content != null) {
            return true;
        }
        if (this.imageUrl != null) {
            return true;
        }
        if (this.topicId != null) {
            return true;
        }
        if (this.majorId != null) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean isMissingKeys() {
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
