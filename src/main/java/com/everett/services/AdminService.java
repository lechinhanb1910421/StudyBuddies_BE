package com.everett.services;

import com.everett.dtos.StatsDTO;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
public interface AdminService {
    public StatsDTO getBriefStats();
    public Integer getUserPostsCount(Long userId) throws UserNotFoundException;

}
