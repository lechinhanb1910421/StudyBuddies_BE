package com.everett.services;

import java.util.List;

import com.everett.dtos.CommentResponseDTO;
import com.everett.dtos.PostReceiveDTO;
import com.everett.dtos.PostResponseDTO;
import com.everett.exceptions.checkedExceptions.DeletePostNotAuthorizedException;
import com.everett.exceptions.checkedExceptions.EmptyCommentException;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.exceptions.checkedExceptions.EmptyReactionException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Post;
import com.everett.models.User;

public interface PostService {

    public void createPost(PostReceiveDTO payload, String email) throws UserNotFoundException;

    public PostResponseDTO getPostResponseById(Long id) throws EmptyEntityException;

    public Post getPostById(Long id) throws EmptyEntityException;

    public List<PostResponseDTO> getAllPosts();

    public List<PostResponseDTO> getAllUserPosts(Long userId) throws UserNotFoundException;

    public void deletePost(Long id, String email, String loginName)
            throws DeletePostNotAuthorizedException, EmptyEntityException;

    public void updatePost(Long id, PostReceiveDTO payload) throws EmptyEntityException;

    public List<PostResponseDTO> seachPostsByKeywords(String keywords);

    public void reactPost(Long id, String email);

    public void removeReactPost(Long id, String email);

    public List<User> getAllPostReation(Long id) throws EmptyReactionException, EmptyEntityException;

    public List<CommentResponseDTO> getCommentsByPostId(Long postId) throws EmptyCommentException, EmptyEntityException;
}
