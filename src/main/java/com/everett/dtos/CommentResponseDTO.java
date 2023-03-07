package com.everett.dtos;

import com.everett.models.Comment;

public class CommentResponseDTO {
    private Long commentId;
    private String content;
    private Long postId;
    private Long userId;

    public CommentResponseDTO() {
    }

    public CommentResponseDTO(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
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

}
