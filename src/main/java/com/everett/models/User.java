package com.everett.models;

import java.sql.Timestamp;
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

import com.everett.utils.TimestampDeserializer;
import com.everett.utils.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "Users")
@Table(name = "Users", schema = "PUBLIC")
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

    // @Column(name = "userRole", nullable = false)
    // private String userRole;

    @Column(name = "accountStatus", nullable = false)
    private String accountStatus;

    @OneToMany(mappedBy = "user", targetEntity = Post.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Post> posts;

    public User() {
    }

    public User(String loginName, String givenName, String familyName, String email, Timestamp createdDate,
            String accountStatus) {
        this.loginName = loginName;
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.createdDate = createdDate;
        // this.userRole = userRole;
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

    // public String getUserRole() {
    // return userRole;
    // }

    // public void setUserRole(String userRole) {
    // this.userRole = userRole;
    // }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Override
    public String toString() {
        return "[User: " + userId + ", email: " + email + ", userName: " + givenName + " " + familyName
                + ", loginName: " + loginName + "]";
    }
}
