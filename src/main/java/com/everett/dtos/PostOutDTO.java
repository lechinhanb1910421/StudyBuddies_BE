package com.everett.dtos;

import com.everett.models.Post;

public class PostOutDTO {
    private Long id;
    private Long userId;
    private String content;
    private String topicName;
    private String majorName;

    public PostOutDTO() {
    }

    public PostOutDTO(Long id, Long userId, String content, String topicName, String majorName) {
        this.userId = userId;
        this.content = content;
        this.topicName = topicName;
        this.majorName = majorName;
    }

    public PostOutDTO(Post post) {
        this.id = post.getPostId();
        this.userId = post.getUserId();
        this.content = post.getContent();
        this.topicName = post.getTopic().getTopicName();
        this.majorName = post.getMajor().getMajorName();
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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
