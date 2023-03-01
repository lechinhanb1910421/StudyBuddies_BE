package com.everett.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.inject.Inject;

import com.everett.daos.MajorDAO;
import com.everett.daos.PostDAO;
import com.everett.daos.TopicDAO;
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
    public void createPost(Post post, Long topicId, Long majorId) {
        Timestamp createdTime = post.getCreatedTime();
        if (createdTime == null) {
            createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        }
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
        try {
            postDAO.createPost(post.getUserId(), createdTime, post.getContent(), post.getAudienceMode(), topic, major);
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
    public void updatePost(Long id, Post post, Long topicId, Long majorId) {
        Post oldPost = getPostById(id);
        post.setPostId(id);
        post.setCreatedTime(oldPost.getCreatedTime());
        if (topicId == null) {
            post.setTopic(oldPost.getTopic());
        } else {
            try {
                post.setTopic(topicDAO.getTopicById(topicId));
            } catch (TopicNotFoundException e) {
                throw new TopicNotFoundWebException(topicId);
            }
        }
        if (majorId == null) {
            post.setMajor(oldPost.getMajor());
        } else {
            try {
                post.setMajor(majorDAO.getMajorById(majorId));
            } catch (MajorNotFoundException e) {
                throw new MajorNotFoundWebException(majorId);
            }
        }
        postDAO.updatePost(post);
    }

    @Override
    public List<Post> seachPostsByKeywords(String keywords) {
        return postDAO.seachPostsByKeywords(keywords);
    }

}
