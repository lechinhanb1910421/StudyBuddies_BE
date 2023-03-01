package com.everett.services;

import java.util.List;

import com.everett.models.Post;

public interface PostService {

    public void createPost(Post post, Long topicId, Long majorId);

    public Post getPostById(Long id);

    public List<Post> getAllPosts();

    public void deletePost(Long id);

    public void updatePost(Long id, Post post, Long topicId, Long majorId);

    public List<Post> seachPostsByKeywords(String keywords);

}
