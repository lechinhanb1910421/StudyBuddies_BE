package com.everett.services;

import java.util.List;

import com.everett.dtos.CommentResponseDTO;
import com.everett.exceptions.checkedExceptions.CommentNotFoundException;

public interface CommentService {
    public void addComment(Long postId, String content, String email);

    public List<CommentResponseDTO> getAllComments();

    public void deleteComment(Long cmtId) throws CommentNotFoundException;
}
