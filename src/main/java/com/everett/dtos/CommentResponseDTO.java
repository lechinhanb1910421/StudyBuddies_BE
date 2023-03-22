package com.everett.dtos;

import java.sql.Timestamp;

import com.everett.models.Comment;
import com.everett.utils.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class CommentResponseDTO {
    private Long commentId;
    private String content;

    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp createdTime;
    private Long postId;
    private Long userId;

    public CommentResponseDTO() {
    }

    public CommentResponseDTO(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.createdTime = comment.getCreatedTime();
        this.postId = comment.getPost().getPostId();
        this.userId = comment.getUser().getUserId();
    }

    public CommentResponseDTO(Long commentId, String content, Long postId, Long userId) {
        this.commentId = commentId;
        this.content = content;
        this.postId = postId;
        this.userId = userId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

}
