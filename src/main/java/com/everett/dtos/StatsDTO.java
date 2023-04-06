package com.everett.dtos;

public class StatsDTO {
    Long numOfUsers;
    Long numOfPosts;

    public StatsDTO(Long numOfUsers, Long numOfPosts) {
        this.numOfUsers = numOfUsers;
        this.numOfPosts = numOfPosts;
    }

    public StatsDTO() {
    }

    public Long getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(Long numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    public Long getNumOfPosts() {
        return numOfPosts;
    }

    public void setNumOfPosts(Long numOfPosts) {
        this.numOfPosts = numOfPosts;
    }

}
