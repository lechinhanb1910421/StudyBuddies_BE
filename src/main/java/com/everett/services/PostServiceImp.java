package com.everett.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.everett.daos.MajorDAO;
import com.everett.daos.PostDAO;
import com.everett.daos.TopicDAO;
import com.everett.dtos.PostDTO;
import com.everett.dtos.PostOutDTO;
import com.everett.exceptions.EmptyEntityException;
import com.everett.exceptions.IdNotFoundException;
import com.everett.exceptions.InternalServerError;
import com.everett.exceptions.MajorNotFoundException;
import com.everett.exceptions.MajorNotFoundWebException;
import com.everett.exceptions.TopicNotFoundException;
import com.everett.exceptions.TopicNotFoundWebException;
import com.everett.models.Major;
import com.everett.models.Post;
import com.everett.models.Topic;

public class PostServiceImp implements PostService {

    @Inject
    PostDAO postDAO;

    @Inject
    TopicDAO topicDAO;

    @Inject
    MajorDAO majorDAO;

    @Override
    public void createPost(PostDTO payload) {
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        Long topicId = payload.getTopicId();
        Long majorId = payload.getMajorId();
        Topic topic = null;
        if (topicId == null) {
            topic = topicDAO.getDefaultTopic();
        } else {
            try {
                topic = topicDAO.getTopicById(topicId);
            } catch (TopicNotFoundException e) {
                throw new TopicNotFoundWebException(topicId);
            }
        }
        Major major = null;
        if (majorId == null) {
            major = majorDAO.getDefaultMajor();
        } else {
            try {
                major = majorDAO.getMajorById(majorId);
            } catch (MajorNotFoundException e) {
                throw new MajorNotFoundWebException(majorId);
            }
        }
        Post newPost = new Post(payload.getUserId(), createdTime, payload.getContent(), payload.getAudienceMode(),
                topic, major);
        try {
            postDAO.createPost(newPost);
        } catch (Exception ex) {
            throw new InternalServerError(ex.getMessage());
        }
    }

    @Override
    public PostOutDTO getPostOutById(Long id) {
        return new PostOutDTO(getPostById(id));
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
    public List<PostOutDTO> getAllPosts() {
        List<Post> postList = postDAO.getAllPosts();
        List<PostOutDTO> results = new ArrayList<>();
        for (Post post : postList) {
            results.add(new PostOutDTO(post));
        }
        return results;

    }

    @Override
    public void deletePost(Long id) {
        getPostById(id);
        postDAO.deletePost(id);
    }

    @Override
    public void updatePost(Long id, PostDTO payload) {
        Post oldPost = getPostById(id);
        Long topicId = payload.getTopicId();
        Long majorId = payload.getMajorId();
        if (topicId != null) {
            try {
                oldPost.setTopic(topicDAO.getTopicById(topicId));
            } catch (TopicNotFoundException e) {
                throw new TopicNotFoundWebException(topicId);
            }
        }
        if (majorId != null) {
            try {
                oldPost.setMajor(majorDAO.getMajorById(majorId));
            } catch (MajorNotFoundException e) {
                throw new MajorNotFoundWebException(majorId);
            }
        }
        postDAO.updatePost(oldPost);
    }

    @Override
    public List<PostOutDTO> seachPostsByKeywords(String keywords) {
        List<Post> postList = postDAO.seachPostsByKeywords(keywords);
        List<PostOutDTO> results = new ArrayList<>();
        for (Post post : postList) {
            results.add(new PostOutDTO(post));
        }
        return results;
    }

}
