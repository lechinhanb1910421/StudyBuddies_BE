package com.everett.services;

import java.util.List;

import javax.ws.rs.core.SecurityContext;

import com.everett.dtos.CommentResponseDTO;
import com.everett.exceptions.checkedExceptions.CommentNotFoundException;

public interface CommentService {
    public void addComment(Long postId, String content, SecurityContext context);

    public List<CommentResponseDTO> getAllComments();

    public void deleteComment(Long cmtId) throws CommentNotFoundException;
}
