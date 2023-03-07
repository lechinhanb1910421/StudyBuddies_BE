package com.everett.dtos;

import com.everett.models.Post;

public class PostResponseDTO {
    private Long postId;
    private Long userId;
    private String content;
    private String topicName;
    private String majorName;
    private Long reactsCount;
    private Long commentsCount;

    public PostResponseDTO() {
    }

    public PostResponseDTO(Long postId, Long userId, String content, String topicName, String majorName,
            Long reactsCount, Long commentsCount) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.topicName = topicName;
        this.majorName = majorName;
        this.reactsCount = reactsCount;
        this.commentsCount = commentsCount;
    }

    public PostResponseDTO(Post post) {
        this.postId = post.getPostId();
        this.userId = post.getOwnerUser().getUserId();
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

    public Long getPostId() {
        return postId;
    }

    public void setpostId(Long postId) {
        this.postId = postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getReactsCount() {
        return reactsCount;
    }

    public void setReactsCount(Long reactsCount) {
        this.reactsCount = reactsCount;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
    }

}
