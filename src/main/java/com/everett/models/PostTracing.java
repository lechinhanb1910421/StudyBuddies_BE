package com.everett.models;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.everett.models.type.PostTracingType;

@Entity(name = "Post_tracing")
@Table(name = "Post_tracing", schema = "PUBLIC")
public class PostTracing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pTracingId", nullable = false)
    private Long pTracingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

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

    public PostTracing(User user, Post post, PostTracingType eventType, String message, String pictureUrls,
            String postContent, Timestamp createdAt, Long topicId, Long majorId) {
        this.user = user;
        this.post = post;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public String getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(String pictureUrls) {
        this.pictureUrls = pictureUrls;
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

}
