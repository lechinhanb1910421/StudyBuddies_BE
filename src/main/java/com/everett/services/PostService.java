package com.everett.services;

import java.util.List;

import com.everett.dtos.PostDTO;
import com.everett.dtos.PostOutDTO;
import com.everett.models.Post;

public interface PostService {

    public void createPost(PostDTO payload);

    public PostOutDTO getPostOutById(Long id);

    public Post getPostById(Long id);

    public List<PostOutDTO> getAllPosts();

    public void deletePost(Long id);

    public void updatePost(Long id, PostDTO payload);

    public List<PostOutDTO> seachPostsByKeywords(String keywords);

}
