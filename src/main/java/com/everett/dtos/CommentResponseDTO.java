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
    private String userFullName;
    private String userGivenName;
    private String userFamilyName;
    private String userAvatarUrl;

    public CommentResponseDTO() {
    }

    public CommentResponseDTO(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.createdTime = comment.getCreatedTime();
        this.postId = comment.getPost().getPostId();
        this.userId = comment.getUser().getUserId();
        this.userGivenName = comment.getUser().getGivenName();
        this.userFamilyName = comment.getUser().getFamilyName();
        this.userAvatarUrl = comment.getUser().getAvatars().iterator().next().getAvaUrl();
        this.userFullName = this.userGivenName + " " + this.userFamilyName;
    }

    public CommentResponseDTO(Long commentId, String content, Long postId, Long userId, String userGivenName,
            String userFamilyName, String userAvatarUrl) {
        this.commentId = commentId;
        this.content = content;
        this.postId = postId;
        this.userId = userId;
        this.userGivenName = userGivenName;
        this.userFamilyName = userFamilyName;
        this.userAvatarUrl = userAvatarUrl;
        this.userFullName = this.userGivenName + " " + this.userFamilyName;
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

    public String getUserGivenName() {
        return userGivenName;
    }

    public void setUserGivenName(String userGivenName) {
        this.userGivenName = userGivenName;
    }

    public String getUserFamilyName() {
        return userFamilyName;
    }

    public void setUserFamilyName(String userFamilyName) {
        this.userFamilyName = userFamilyName;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

}
