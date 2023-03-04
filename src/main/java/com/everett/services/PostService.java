package com.everett.services;

import java.util.List;

import javax.ws.rs.core.SecurityContext;

import com.everett.dtos.PostReceiveDTO;
import com.everett.dtos.PostResponseDTO;
import com.everett.exceptions.UserNotFoundException;
import com.everett.models.Post;

public interface PostService {

    public void createPost(PostReceiveDTO payload, SecurityContext securityContext) throws UserNotFoundException;

    public PostResponseDTO getPostResponseById(Long id);

    public Post getPostById(Long id);

    public List<PostResponseDTO> getAllPosts();

    public List<PostResponseDTO> getAllUserPosts(SecurityContext securityContext);

    public void deletePost(Long id);

    public void updatePost(Long id, PostReceiveDTO payload);

    public List<PostResponseDTO> seachPostsByKeywords(String keywords);

}
