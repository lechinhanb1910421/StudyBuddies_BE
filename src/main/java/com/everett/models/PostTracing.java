package com.everett.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.everett.models.type.PostTracingType;

@Entity(name = "Post_tracing")
@Table(name = "Post_tracing", schema = "PUBLIC")
public class PostTracing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pTracingId", nullable = false)
    private Long pTracingId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "postId", nullable = false)
    private Long postId;

    @Column(name = "eventType")
    @Enumerated(EnumType.STRING)
    private PostTracingType eventType;

    @Column(name = "message")
    private String message;

    @Column(name = "pictureUrls")
    private String pictureUrls;

    @Column(name = "postContent")
    private String postContent;

    @Column(name = "createdAt", nullable = false)
    private Timestamp createdAt;

    @Column(name = "topicId")
    private Long topicId;

    @Column(name = "majorId")
    private Long majorId;

    public PostTracing() {
    }

    public PostTracing(Long userId, String userEmail, Long postId, PostTracingType eventType, String message,
            String pictureUrls, String postContent, Timestamp createdAt, Long topicId, Long majorId) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.postId = postId;
        this.eventType = eventType;
        this.message = message;
        this.pictureUrls = pictureUrls;
        this.postContent = postContent;
        this.createdAt = createdAt;
        this.topicId = topicId;
        this.majorId = majorId;
    }

    public Long getpTracingId() {
        return pTracingId;
    }

    public void setpTracingId(Long pTracingId) {
        this.pTracingId = pTracingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public PostTracingType getEventType() {
        return eventType;
    }

    public void setEventType(PostTracingType eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(String pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }



   

}
