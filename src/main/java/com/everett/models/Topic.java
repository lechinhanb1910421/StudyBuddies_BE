package com.everett.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity(name = "Topics")
@Table(name = "Topics", schema = "PUBLIC")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topicId", nullable = false)
    private Long topicId;

    @Column(name = "topicName", nullable = false)
    private String topicName;

    @Column(name = "topicDescription", nullable = false)
    private String topicDescription;

    @Column(name = "followers", nullable = false)
    private Long followers;

    @OneToMany(mappedBy = "topic", targetEntity = Post.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Post> posts;

    public Topic() {
    }

    public Topic(String topicName, String topicDescription, Long followers) {
        this.topicName = topicName;
        this.topicDescription = topicDescription;
        this.followers = followers;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public Long getFollowers() {
        return followers;
    }

    public void setFollowers(Long followers) {
        this.followers = followers;
    }

    @Override
    public String toString() {
        return "[Post" + topicName + " " + topicDescription + " " + followers + "]";
    }

}
