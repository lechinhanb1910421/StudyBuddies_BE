package com.everett.models;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.everett.utils.TimestampDeserializer;
import com.everett.utils.TimestampSerializer;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "createdTime", nullable = false)
    @JsonDeserialize(using = TimestampDeserializer.class)
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp createdTime;

    @Column(name = "content", nullable = false)
    @Field(index = Index.YES, store = Store.NO)
    private String content;

    @Column(name = "audienceMode", nullable = false)
    private String audienceMode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topicId", nullable = false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "majorId", nullable = false)
    private Major major;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post_reaction", joinColumns = {
            @JoinColumn(name = "postId", nullable = false, updatable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "userId", nullable = false, updatable = false) })
    private Set<User> reactions = new HashSet<User>();

    @OneToMany(mappedBy = "post", targetEntity = Comment.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Comment> comments = new HashSet<Comment>();

    public Post() {
    }

    public Post(User user, Timestamp createdTime, String content, String audienceMode, Topic topic, Major major) {
        this.user = user;
        this.createdTime = createdTime;
        this.content = content;
        this.audienceMode = audienceMode;
        this.topic = topic;
        this.major = major;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public User getOwnerUser() {
        return user;
    }

    public void setOwnerUser(User user) {
        this.user = user;
    }

    public Set<User> getReactedUser() {
        return reactions;
    }

    public void setReactedUser(User user) {
        this.reactions.add(user);
    }

    public void unsetReactedUser(User user) {
        this.reactions.remove(user);
    }

    // public Set<Comment> getCommentUser() {
    // return comments;
    // }

    // public void setComment(Comment comment) {
    // this.comments.add(comment);
    // }

    // public void unsetReactedUser(Comment comment) {
    // this.comments.remove(comment);
    // }
}
