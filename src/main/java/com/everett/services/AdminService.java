package com.everett.services;

import com.everett.dtos.StatsDTO;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
public interface AdminService {
    public StatsDTO getBriefStats();
    public Integer getUserPostsCount(Long userId) throws UserNotFoundException;
    public void deletePostById(Long postId) throws EmptyEntityException;
}
