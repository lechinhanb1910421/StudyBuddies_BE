package com.everett.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "avatars")
@Table(name = "avatars", schema = "PUBLIC")
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ava_id", nullable = false)
    private Long avaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ava_url", nullable = false)
    private String avaUrl;

    public Avatar() {
    }

    public Avatar(String avaUrl) {
        this.avaUrl = avaUrl;
    }

    @Override
    public String toString() {
        return "Avatar [avaId=" + avaId + ", avaUrl=" + avaUrl + "]";
    }

    public Long getAvaId() {
        return avaId;
    }

    public void setAvaId(Long avaId) {
        this.avaId = avaId;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAvaUrl() {
        return avaUrl;
    }

    public void setAvaUrl(String avaUrl) {
        this.avaUrl = avaUrl;
    }

}
