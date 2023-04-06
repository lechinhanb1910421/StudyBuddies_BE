package com.everett.services;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.everett.daos.AvatarDAO;
import com.everett.daos.PostDAO;
import com.everett.daos.UserDAO;
import com.everett.dtos.StatsDTO;

@Stateless
public class AdminServiceImp implements AdminService {

    @Inject
    UserService userService;
    @Inject
    PostService postService;

    @Inject
    UserDAO userDAO;

    @Inject
    AvatarDAO avatarDAO;

    @Inject
    PostDAO postDAO;

    @Override
    public StatsDTO getBriefStats() {
        StatsDTO stats = new StatsDTO();
        stats.setNumOfPosts(postService.getCountPosts());
        stats.setNumOfUsers(userService.getCountUsers());
        return stats;
    }

}
