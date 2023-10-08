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

import com.everett.models.type.CommentTracingType;

@Entity(name = "Comment_tracing")
@Table(name = "Comment_tracing", schema = "PUBLIC")
public class CommentTracing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cTracingId", nullable = false)
    private Long cTracingId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "postId", nullable = false)
    private Long postId;

    @Column(name = "eventType")
    @Enumerated(EnumType.STRING)
    private CommentTracingType eventType;

    @Column(name = "message")
    private String message;

    @Column(name = "commentContent")
    private String commentContent;

    @Column(name = "createdAt", nullable = false)
    private Timestamp createdAt;

    public CommentTracing() {
    }

    @Override
    public String toString() {
        return "CommentTracing [cTracingId=" + cTracingId + ", userId=" + userId + ", userEmail=" + userEmail
                + ", postId=" + postId + ", eventType=" + eventType + ", message=" + message + ", commentContent="
                + commentContent + ", createdAt=" + createdAt + "]";
    }

    public CommentTracing(Long userId, String userEmail, Long postId, CommentTracingType eventType, String message,
            String commentContent, Timestamp createdAt) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.postId = postId;
        this.eventType = eventType;
        this.message = message;
        this.commentContent = commentContent;
        this.createdAt = createdAt;
    }

    public Long getcTracingId() {
        return cTracingId;
    }

    public void setcTracingId(Long cTracingId) {
        this.cTracingId = cTracingId;
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

    public CommentTracingType getEventType() {
        return eventType;
    }

    public void setEventType(CommentTracingType eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
