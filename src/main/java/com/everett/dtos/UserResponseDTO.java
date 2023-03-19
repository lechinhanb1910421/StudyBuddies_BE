package com.everett.dtos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everett.models.Avatar;
import com.everett.models.User;
import com.everett.utils.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserResponseDTO {
    private Long userId;
    private String loginName;
    private String givenName;
    private String familyName;
    private String fullName;
    private String email;

    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp createdDate;
    private String accountStatus;
    private List<Avatar> avatars = new ArrayList<Avatar>();

    public UserResponseDTO() {
    }

    public UserResponseDTO(User user) {
        this.userId = user.getUserId();
        this.loginName = user.getLoginName();
        this.givenName = user.getGivenName();
        this.familyName = user.getFamilyName();
        this.fullName = this.givenName + " " + this.familyName;
        this.email = user.getEmail();
        this.createdDate = user.getCreatedDate();
        this.accountStatus = user.getAccountStatus();
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public List<Avatar> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<Avatar> avatars) {
        this.avatars = avatars;
    }

}
