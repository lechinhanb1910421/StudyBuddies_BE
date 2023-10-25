package com.everett.dtos;

import java.sql.Timestamp;

import com.everett.models.Notification;
import com.everett.models.type.NotificationStateType;
import com.everett.utils.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserNotificationDTO {
    private Long notiId;
    private UserResponseDTO sourceUser;
    private String content;
    private String notiType;
    private Timestamp createdAt;
    private String referenceLink;
    private NotificationStateType readStatus;

    public UserNotificationDTO() {
    }

    public UserNotificationDTO(Notification notification) {
        this.notiId = notification.getNotiId();
        this.content = notification.getContent();
        this.notiType = notification.getNotiType();
        this.createdAt = notification.getCreatedAt();
        this.referenceLink = notification.getReferenceLink();
        this.readStatus = notification.getReadStatus();
    }

    public Long getNotiId() {
        return notiId;
    }

    public void setNotiId(Long notiId) {
        this.notiId = notiId;
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

    @JsonSerialize(using = TimestampSerializer.class)
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

    public UserResponseDTO getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(UserResponseDTO receiverUser) {
        this.sourceUser = receiverUser;
    }

    public NotificationStateType getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(NotificationStateType readStatus) {
        this.readStatus = readStatus;
    }

}
