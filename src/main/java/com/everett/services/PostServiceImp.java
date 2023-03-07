package com.everett.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;

import com.everett.daos.CommentDAO;
import com.everett.daos.MajorDAO;
import com.everett.daos.PostDAO;
import com.everett.daos.TopicDAO;
import com.everett.dtos.PostReceiveDTO;
import com.everett.dtos.PostResponseDTO;
import com.everett.exceptions.EmptyCommentException;
import com.everett.exceptions.EmptyEntityException;
import com.everett.exceptions.EmptyReactionException;
import com.everett.exceptions.IdNotFoundException;
import com.everett.exceptions.InternalServerError;
import com.everett.exceptions.MajorNotFoundException;
import com.everett.exceptions.MajorNotFoundWebException;
import com.everett.exceptions.TopicNotFoundException;
import com.everett.exceptions.TopicNotFoundWebException;
import com.everett.exceptions.UserNotFoundException;
import com.everett.models.Comment;
import com.everett.models.Major;
import com.everett.models.Post;
import com.everett.models.Topic;
import com.everett.models.User;

public class PostServiceImp implements PostService {
    @Inject
    PostDAO postDAO;

    @Inject
    TopicDAO topicDAO;

    @Inject
    MajorDAO majorDAO;

    @Inject
    CommentDAO commentDAO;

    @Inject
    UserService userService;

    @Override
    @SuppressWarnings("rawtypes")
    public void createPost(PostReceiveDTO payload, SecurityContext securityContext) throws UserNotFoundException {
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
        KeycloakPrincipal principal = (KeycloakPrincipal) securityContext.getUserPrincipal();
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();
        User user = userService.getUserByEmail(accessToken.getEmail());
        Post newPost = new Post(user, createdTime, payload.getContent(), payload.getAudienceMode(), topic, major);
        try {
            postDAO.createPost(newPost);
        } catch (Exception ex) {
            throw new InternalServerError(ex.getMessage());
        }
    }

    @Override
    public PostResponseDTO getPostResponseById(Long id) {
        return new PostResponseDTO(getPostById(id));
    }

    @Override
    public Post getPostById(Long id) {
        if (id < 1) {
            throw new IdNotFoundException(id);
        }
        try {
            return postDAO.getPostById(id);
        } catch (EmptyEntityException e) {
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        List<Post> postList = postDAO.getAllPosts();
        List<PostResponseDTO> results = new ArrayList<>();
        for (Post post : postList) {
            PostResponseDTO responseDTO = new PostResponseDTO(post);
            long reactionCount = 0;
            long commentCount = 0;
            try {
                reactionCount = getAllPostReation(post.getPostId()).size();
                commentCount = getCommentsByPostId(post.getPostId()).size();
                responseDTO.setReactsCount(reactionCount);
                responseDTO.setCommentsCount(commentCount);
            } catch (EmptyReactionException e) {
                responseDTO.setReactsCount(0l);
            } catch (EmptyCommentException e) {
                responseDTO.setCommentsCount(0l);
            }
            results.add(responseDTO);
        }
        return results;

    }

    @Override
    public void deletePost(Long id) {
        getPostById(id);
        postDAO.deletePost(id);
    }

    @Override
    public void updatePost(Long id, PostReceiveDTO payload) {
        Post oldPost;
        oldPost = getPostById(id);
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
    public List<PostResponseDTO> seachPostsByKeywords(String keywords) {
        List<Post> postList = postDAO.seachPostsByKeywords(keywords);
        List<PostResponseDTO> results = new ArrayList<>();
        for (Post post : postList) {
            results.add(new PostResponseDTO(post));
        }
        return results;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List<PostResponseDTO> getAllUserPosts(SecurityContext securityContext) {
        KeycloakPrincipal principal = (KeycloakPrincipal) securityContext.getUserPrincipal();
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();
        List<Post> postList = postDAO.getAllUserPosts(accessToken.getEmail());
        List<PostResponseDTO> results = new ArrayList<>();
        for (Post post : postList) {
            PostResponseDTO responseDTO = new PostResponseDTO(post);
            long reactionCount = 0;
            long commentCount = 0;
            try {
                reactionCount = getAllPostReation(post.getPostId()).size();
                commentCount = getCommentsByPostId(post.getPostId()).size();
                responseDTO.setReactsCount(reactionCount);
                responseDTO.setCommentsCount(commentCount);
            } catch (EmptyReactionException e) {
                responseDTO.setReactsCount(0l);
            } catch (EmptyCommentException e) {
                responseDTO.setCommentsCount(0l);
            }
            results.add(responseDTO);
        }
        return results;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void reactPost(Long id, SecurityContext securityContext) {
        KeycloakPrincipal principal = (KeycloakPrincipal) securityContext.getUserPrincipal();
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();
        String email = accessToken.getEmail();
        try {
            User user = userService.getUserByEmail(email);
            postDAO.reactPost(id, user);
        } catch (UserNotFoundException e) {
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void removeReactPost(Long id, SecurityContext securityContext) {
        KeycloakPrincipal principal = (KeycloakPrincipal) securityContext.getUserPrincipal();
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();
        String email = accessToken.getEmail();
        try {
            User user = userService.getUserByEmail(email);
            postDAO.removeReactPost(id, user);
        } catch (UserNotFoundException e) {
        }
    }

    @Override
    public List<User> getAllPostReation(Long id) throws EmptyReactionException {
        return postDAO.getAllPostReation(id);
    }

    public List<Comment> getCommentsByPostId(Long postId) throws EmptyCommentException {
        return commentDAO.getCommentsByPostId(postId);

    }

}
