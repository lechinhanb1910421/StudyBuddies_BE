package com.everett.services;

import java.util.List;

import javax.ws.rs.core.SecurityContext;

import com.everett.dtos.CommentResponseDTO;
import com.everett.dtos.PostReceiveDTO;
import com.everett.dtos.PostResponseDTO;
import com.everett.exceptions.checkedExceptions.EmptyCommentException;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.exceptions.checkedExceptions.EmptyReactionException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Post;
import com.everett.models.User;

public interface PostService {

    public void createPost(PostReceiveDTO payload, SecurityContext securityContext) throws UserNotFoundException;

    public PostResponseDTO getPostResponseById(Long id);

    public Post getPostById(Long id);

    public List<PostResponseDTO> getAllPosts();

    public List<PostResponseDTO> getAllUserPosts(SecurityContext securityContext);

    public void deletePost(Long id);

    public void updatePost(Long id, PostReceiveDTO payload);

    public List<PostResponseDTO> seachPostsByKeywords(String keywords);

    public void reactPost(Long id, SecurityContext securityContext);

    public void removeReactPost(Long id, SecurityContext securityContext);

    public List<User> getAllPostReation(Long id) throws EmptyReactionException, EmptyEntityException;

    public List<CommentResponseDTO> getCommentsByPostId(Long postId) throws EmptyCommentException, EmptyEntityException;
}
