package com.everett.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.LongBridge;

import com.everett.util.TimestampDeserializer;
import com.everett.util.TimestampSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "Posts")
@Table(name = "Posts", schema = "PUBLIC")
@Indexed
public class Post {
    @Id
    @DocumentId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId", nullable = false)
    private Long postId;

    @Column(name = "userId", nullable = false)
    @FieldBridge(impl = LongBridge.class)
    @Field
    private Long userId;

    @Column(name = "createdTime", nullable = false)
    @JsonDeserialize(using = TimestampDeserializer.class)
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp createdTime;

    @Column(name = "content", nullable = false)
    @Field(index = Index.YES, store = Store.NO)
    private String content;

    @Column(name = "audienceMode", nullable = false)
    private String audienceMode;

    public Post() {
    }

    public Post(Long userId, Timestamp createdTime, String content, String audienceMode) {
        this.userId = userId;
        this.createdTime = createdTime;
        this.content = content;
        this.audienceMode = audienceMode;
    }

    @JsonIgnore
    public boolean isMissingKeys() {
        if (this.userId == null) {
            return true;
        }
        if (this.createdTime == null) {
            return true;
        }
        if (this.content == null) {
            return true;
        }
        if (this.audienceMode == null) {
            return true;
        }
        return false;

    }

    @JsonIgnore
    public boolean isEmpty() {
        if (this.userId != null) {
            return false;
        }
        if (this.createdTime != null) {
            return false;
        }
        if (this.content != null) {
            return false;
        }
        if (this.audienceMode != null) {
            return false;
        }
        return true;
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

}
