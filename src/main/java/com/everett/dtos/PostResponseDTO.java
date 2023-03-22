package com.everett.dtos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everett.models.Post;
import com.everett.utils.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PostResponseDTO {
    private Long postId;
    private Long userId;

    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp createdTime;
    private String content;
    private String topicName;
    private String majorName;
    // private Long reactsCount;
    // private Long commentsCount;
    private List<String> picUrls = new ArrayList<String>();

    public PostResponseDTO() {
    }

    public PostResponseDTO(Long postId, Long userId, Timestamp createdTime, String content, String topicName,
            String majorName) {
        this.postId = postId;
        this.userId = userId;
        this.createdTime = createdTime;
        this.content = content;
        this.topicName = topicName;
        this.majorName = majorName;
    }

    public PostResponseDTO(Post post) {
        this.postId = post.getPostId();
        this.userId = post.getOwnerUser().getUserId();
        this.createdTime = post.getCreatedTime();
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

    // public Long getReactsCount() {
    // return reactsCount;
    // }

    // public void setReactsCount(Long reactsCount) {
    // this.reactsCount = reactsCount;
    // }

    // public Long getCommentsCount() {
    // return commentsCount;
    // }

    // public void setCommentsCount(Long commentsCount) {
    // this.commentsCount = commentsCount;
    // }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }

    public void setPicUrls(String picUrls) {
        this.picUrls.add(picUrls);
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
}
