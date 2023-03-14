package com.everett.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;

import com.everett.daos.CommentDAO;
import com.everett.daos.MajorDAO;
import com.everett.daos.PostDAO;
import com.everett.daos.TopicDAO;
import com.everett.daos.UserDAO;
import com.everett.dtos.CommentResponseDTO;
import com.everett.dtos.PostReceiveDTO;
import com.everett.dtos.PostResponseDTO;
import com.everett.exceptions.checkedExceptions.EmptyCommentException;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.exceptions.checkedExceptions.EmptyReactionException;
import com.everett.exceptions.checkedExceptions.MajorNotFoundException;
import com.everett.exceptions.checkedExceptions.TopicNotFoundException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.webExceptions.IdNotFoundException;
import com.everett.exceptions.webExceptions.InternalServerError;
import com.everett.exceptions.webExceptions.MajorNotFoundWebException;
import com.everett.exceptions.webExceptions.TopicNotFoundWebException;
import com.everett.models.Comment;
import com.everett.models.Major;
import com.everett.models.Picture;
import com.everett.models.Post;
import com.everett.models.Topic;
import com.everett.models.User;

@Stateless
public class PostServiceImp implements PostService {
    private static final Logger logger = LogManager.getLogger(PostService.class);
    @Inject
    PostDAO postDAO;

    @Inject
    TopicDAO topicDAO;

    @Inject
    MajorDAO majorDAO;

    @Inject
    UserDAO userDAO;

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
        logger.info("CREATING POST FOR USER: " + user.getLoginName());
        Post newPost = new Post(user, createdTime, payload.getContent(), payload.getAudienceMode(), topic, major);
        try {
            postDAO.createPost(newPost);
        } catch (Exception ex) {
            throw new InternalServerError(ex.getMessage());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PostResponseDTO getPostResponseById(Long id) {
        Post post = getPostById(id);
        PostResponseDTO result = new PostResponseDTO(post);
        result.setReactsCount(postDAO.getAllPostReactionsCount(id));
        result.setCommentsCount(postDAO.getAllPostCommentsCount(id));
        Set<Picture> pictures = post.getPictures();
        pictures.forEach((pic) -> {
            result.setPicUrls(pic.getPicUrl());
        });
        return result;
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<PostResponseDTO> getAllPosts() {
        List<Post> postList = postDAO.getAllPosts();
        List<PostResponseDTO> results = new ArrayList<>();
        for (Post post : postList) {
            PostResponseDTO responseDTO = new PostResponseDTO(post);
            responseDTO.setReactsCount(postDAO.getAllPostReactionsCount(post.getPostId()));
            responseDTO.setCommentsCount(postDAO.getAllPostCommentsCount(post.getPostId()));
            Set<Picture> pictures = post.getPictures();
            pictures.forEach((pic) -> {
                responseDTO.setPicUrls(pic.getPicUrl());
            });
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
            PostResponseDTO responseDTO = new PostResponseDTO(post);
            responseDTO.setReactsCount(Long.valueOf(post.getReactedUser().size()));
            responseDTO.setCommentsCount(Long.valueOf(post.getCommentUser().size()));
            Set<Picture> pictures = post.getPictures();
            pictures.forEach((pic) -> {
                responseDTO.setPicUrls(pic.getPicUrl());
            });
            results.add(responseDTO);
        }
        return results;
    }

    @Override
    @SuppressWarnings("rawtypes")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<PostResponseDTO> getAllUserPosts(SecurityContext securityContext) {
        KeycloakPrincipal principal = (KeycloakPrincipal) securityContext.getUserPrincipal();
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();
        User user;
        List<Post> postList;
        List<PostResponseDTO> results = new ArrayList<PostResponseDTO>();
        try {
            user = userDAO.getUserByEmail(accessToken.getEmail());
            Set<Post> setPost = new HashSet<Post>();
            setPost = user.getPosts();
            postList = new ArrayList<Post>(setPost);
            logger.info("GET POSTS FROM USER: " + user.getLoginName());
            for (Post post : postList) {
                PostResponseDTO responseDTO = new PostResponseDTO(post);
                responseDTO.setReactsCount(Long.valueOf(post.getReactedUser().size()));
                responseDTO.setCommentsCount(Long.valueOf(post.getCommentUser().size()));
                Set<Picture> pictures = post.getPictures();
                pictures.forEach((pic) -> {
                    responseDTO.setPicUrls(pic.getPicUrl());
                });
                results.add(responseDTO);
            }
        } catch (UserNotFoundException e) {
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<User> getAllPostReation(Long id) throws EmptyEntityException, EmptyReactionException {
        Set<User> users = postDAO.getPostById(id).getReactedUser();
        if (users.size() == 0) {
            throw new EmptyReactionException();
        } else {
            return new ArrayList<User>(users);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<CommentResponseDTO> getCommentsByPostId(Long postId)
            throws EmptyCommentException, EmptyEntityException {
        Post post = postDAO.getPostById(postId);
        Set<Comment> comments = new HashSet<Comment>(post.getCommentUser());
        if (comments.size() == 0) {
            throw new EmptyCommentException();
        }
        List<CommentResponseDTO> results = new ArrayList<CommentResponseDTO>();
        comments.forEach((elem) -> {
            results.add(new CommentResponseDTO(elem));
        });
        return results;

    }

}
