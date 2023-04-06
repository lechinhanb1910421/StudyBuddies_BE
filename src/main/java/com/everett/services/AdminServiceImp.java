package com.everett.services;

import javax.inject.Inject;

import com.everett.dtos.StatsDTO;

public class AdminServiceImp implements AdminService {

    @Inject
    UserService userService;
    @Inject
    PostService postService;

    @Override
    public StatsDTO getBriefStats() {
        StatsDTO stats = new StatsDTO();
        stats.setNumOfPosts(postService.getCountPosts());
        stats.setNumOfUsers(userService.getCountUsers());
        return stats;
    }
}
