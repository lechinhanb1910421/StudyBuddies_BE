package com.everett.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "Notifications")
@Table(name = "Notifications", schema = "PUBLIC")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notiId", nullable = false)
    private Long notiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId", nullable = false)
    private User receiverUser;

    @Column(name = "content")
    private String content;

    @Column(name = "notiType", nullable = false)
    private String notiType;

    @Column(name = "createdAt", nullable = false)
    private Timestamp createdAt;

    @Column(name = "referenceLink", nullable = false)
    private String referenceLink;

    public Notification() {
    }

    public Notification(User receiver, String content, String notiType, Timestamp createdAt, String referenceLink) {
        this.receiverUser = receiver;
        this.content = content;
        this.notiType = notiType;
        this.createdAt = createdAt;
        this.referenceLink = referenceLink;
    }

    @Override
    public String toString() {
        return "Notification [notiId=" + notiId + ", receiverUser=" + receiverUser + ", content=" + content
                + ", notiType=" + notiType + ", createdAt=" + createdAt + ", referenceLink=" + referenceLink + "]";
    }

    public Long getNotiId() {
        return notiId;
    }

    public void setNotiId(Long notiId) {
        this.notiId = notiId;
    }

    public User getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotiType() {
        return notiType;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }

}
