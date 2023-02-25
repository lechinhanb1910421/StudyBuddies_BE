package com.everett.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.inject.Inject;

import com.everett.daos.PostDAO;
import com.everett.exceptions.EmptyEntityException;
import com.everett.exceptions.IdNotFoundException;
import com.everett.exceptions.InternalServerError;
import com.everett.models.Post;

public class PostServiceImp implements PostService {

    @Inject
    PostDAO postDAO;

    @Override
    public void createPost(Post post) {
        try {
            Timestamp createdTime = post.getCreatedTime();
            if (createdTime == null) {
                createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
            }
            postDAO.createPost(post.getUserId(), createdTime, post.getContent(), post.getAudienceMode());
        } catch (Exception ex) {
            throw new InternalServerError(ex.getMessage());
        }
    }

    @Override
    public Post getPostById(Long id) {
        if (id < 1) {
            throw new IdNotFoundException(id);
        }
        try {
            return postDAO.getPostById(id);
        } catch (EmptyEntityException ex) {
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public List<Post> getAllPosts() {
        return postDAO.getAllPosts();

    }

    @Override
    public void deletePost(Long id) {
        getPostById(id);
        postDAO.deletePost(id);
    }

    @Override
    public void updatePost(Long id, Post post) {
        Post oldPost = getPostById(id);
        post.setPostId(id);
        post.setCreatedTime(oldPost.getCreatedTime());
        postDAO.updatePost(post);
    }

    @Override
    public List<Post> seachPostsByKeywords(String keywords) {
        return postDAO.seachPostsByKeywords(keywords);
    }

}
