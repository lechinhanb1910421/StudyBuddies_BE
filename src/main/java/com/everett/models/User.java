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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.everett.utils.TimestampDeserializer;
import com.everett.utils.TimestampSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "Users")
@Table(name = "Users", schema = "PUBLIC")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "loginName", nullable = false)
    private String loginName;

    @Column(name = "givenName", nullable = false)
    private String givenName;

    @Column(name = "familyName", nullable = false)
    private String familyName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "createdDate", nullable = false)
    @JsonDeserialize(using = TimestampDeserializer.class)
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp createdDate;

    @Column(name = "accountStatus", nullable = false)
    private String accountStatus;

    @OneToMany(mappedBy = "user", targetEntity = Avatar.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Avatar> avatars = new HashSet<Avatar>();

    @OneToMany(mappedBy = "user", targetEntity = Post.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Post> posts = new HashSet<Post>();

    @ManyToMany(mappedBy = "reactions", fetch = FetchType.LAZY)
    private Set<Post> reactions = new HashSet<Post>();

    @OneToMany(mappedBy = "user", targetEntity = Comment.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Comment> comments = new HashSet<Comment>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", targetEntity = Device.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Device> devices = new HashSet<>();

    @OneToMany(mappedBy = "receiverUser", targetEntity = Notification.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Notification> notifications = new HashSet<Notification>();

    @OneToMany(mappedBy = "user", targetEntity = PostTracing.class, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<PostTracing> tracingRecords = new HashSet<PostTracing>();
    
    public User() {
    }

    public User(String loginName, String givenName, String familyName, String email, Timestamp createdDate,
            String accountStatus) {
        this.loginName = loginName;
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.createdDate = createdDate;
        this.accountStatus = accountStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", loginName=" + loginName + ", givenName=" + givenName + ", familyName="
                + familyName + ", email=" + email + ", createdDate=" + createdDate + ", accountStatus=" + accountStatus
                + ", posts=" + posts + ", reactions=" + reactions + ", comments=" + comments + "]";
    }

    @JsonIgnore
    public Set<Post> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Post> reactions) {
        this.reactions = reactions;
    }

    @JsonIgnore
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @JsonIgnore
    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public void setAvatar(Avatar avatar) {
        this.avatars.add(avatar);
        avatar.setUser(this);
    }

    public void unsetAvatar(Avatar avatar) {
        this.avatars.remove(avatar);
        avatar.setUser(null);
    }

    public Set<Avatar> getAvatars() {
        return avatars;
    }

    public void setAvatarsSet(Set<Avatar> avatars) {
        this.avatars = avatars;
    }

    public void removeAllPic() {
        this.avatars.clear();
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }
    
    public Set<PostTracing> getTracingRecords() {
        return tracingRecords;
    }

    public void setTracingRecords(Set<PostTracing> tracingRecords) {
        this.tracingRecords = tracingRecords;
    }

}
