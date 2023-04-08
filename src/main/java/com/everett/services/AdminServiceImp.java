package com.everett.services;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.everett.daos.AvatarDAO;
import com.everett.daos.PostDAO;
import com.everett.daos.UserDAO;
import com.everett.dtos.StatsDTO;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;

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

    @Override
    public Integer getUserPostsCount(Long userId) throws UserNotFoundException {
        return userDAO.getUserPostsCount(userId);
    }

    @Override
    public void deletePostById(Long postId) throws EmptyEntityException {
        postDAO.getPostById(postId);
        postDAO.deletePost(postId);
    }

}
