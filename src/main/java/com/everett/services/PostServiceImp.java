package com.everett.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.daos.AvatarDAO;
import com.everett.daos.CommentDAO;
import com.everett.daos.MajorDAO;
import com.everett.daos.PostDAO;
import com.everett.daos.PostTracingDAO;
import com.everett.daos.TopicDAO;
import com.everett.daos.UserDAO;
import com.everett.dtos.CommentResponseDTO;
import com.everett.dtos.PostReceiveDTO;
import com.everett.dtos.PostResponseDTO;
import com.everett.dtos.UserResponseDTO;
import com.everett.exceptions.checkedExceptions.DeletePostNotAuthorizedException;
import com.everett.exceptions.checkedExceptions.EmptyCommentException;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.exceptions.checkedExceptions.EmptyReactionException;
import com.everett.exceptions.checkedExceptions.MajorNotFoundException;
import com.everett.exceptions.checkedExceptions.TopicNotFoundException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.webExceptions.InternalServerError;
import com.everett.exceptions.webExceptions.MajorNotFoundWebException;
import com.everett.exceptions.webExceptions.TopicNotFoundWebException;
import com.everett.models.Avatar;
import com.everett.models.Comment;
import com.everett.models.Major;
import com.everett.models.Picture;
import com.everett.models.Post;
import com.everett.models.PostTracing;
import com.everett.models.Topic;
import com.everett.models.User;
import com.everett.models.type.NotificationType;
import com.everett.models.type.PostTracingType;

@Stateless
public class PostServiceImp implements PostService {
    private static final Logger logger = LogManager.getLogger(PostService.class);
    private static final int AVATAR_AVTICE = 1;
    private static final String ADD_POST_MESSASE_TEMPLATE = "User: [%s] email: [%s] create a new post";
    private static final String UPDATE_POST_MESSASE_TEMPLATE = "User: [%s] email: [%s] update post";
    private static final String DELETE_POST_MESSASE_TEMPLATE = "User: [%s] email: [%s] delete post";
    private static final String PICTURE_URL_TEMPLATE = "(%s)[%s] - ";

    @Inject
    PostDAO postDAO;

    @Inject
    TopicDAO topicDAO;

    @Inject
    MajorDAO majorDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    AvatarDAO avatarDAO;

    @Inject
    CommentDAO commentDAO;

    @Inject
    UserService userService;

    @Inject
    UserNotificationService userNotificationService;

    @Inject
    PushNotificationService pushNotificationService;

    @Inject
    PostTracingDAO postTracingDAO;

    @Override
    public void createPost(PostReceiveDTO payload, String email) throws UserNotFoundException {
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
        User user = userService.getUserByEmail(email);
        logger.info("CREATING POST FOR USER: " + user.getLoginName());
        Post newPost = new Post(user, createdTime, payload.getContent(),
                payload.getAudienceMode(), topic, major);
        Picture picture = new Picture(payload.getImageUrl());
        newPost.setPicture(picture);

        try {
            postDAO.createPost(newPost);
            postTracingDAO.createPostTracing(buildPostTracingRecord(newPost, user, PostTracingType.ADD_POST));
        } catch (Exception ex) {
            throw new InternalServerError(ex.getMessage());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PostResponseDTO getPostResponseById(Long id) throws EmptyEntityException {
        Post post = getPostById(id);
        PostResponseDTO result = new PostResponseDTO(post);
        Set<Picture> pictures = post.getPictures();
        pictures.forEach((pic) -> {
            result.setPicUrls(pic.getPicUrl());
        });
        return result;
    }

    @Override
    public Post getPostById(Long id) throws EmptyEntityException {
        if (id < 1) {
            throw new EmptyEntityException(id);
        }
        return postDAO.getPostById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<PostResponseDTO> getAllPosts() {
        List<Post> postList = postDAO.getAllPosts();
        List<PostResponseDTO> results = new ArrayList<>();
        for (Post post : postList) {
            PostResponseDTO responseDTO = new PostResponseDTO(post);
            Set<Picture> pictures = post.getPictures();
            pictures.forEach((pic) -> {
                responseDTO.setPicUrls(pic.getPicUrl());
            });
            results.add(responseDTO);
        }
        return results;

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deletePost(Long id, String email, String loginName)
            throws DeletePostNotAuthorizedException, EmptyEntityException {
        Post post = getPostById(id);
        User owner = post.getOwnerUser();
        if (!owner.getEmail().equals(email) || !owner.getLoginName().equals(loginName)) {
            throw new DeletePostNotAuthorizedException();
        }
        postTracingDAO.createPostTracing(buildPostTracingRecord(post, owner, PostTracingType.DELETE_POST));
        postDAO.deletePost(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updatePost(Long id, PostReceiveDTO payload) throws EmptyEntityException {
        Post oldPost;
        oldPost = getPostById(id);
        Long majorId = null;
        Long topicId = null;
        String newContent = null;
        String imgUrl = null;
        try {
            majorId = payload.getMajorId();
            topicId = payload.getTopicId();
            newContent = payload.getContent();
            imgUrl = payload.getImageUrl();

        } catch (NullPointerException ex) {

        }
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
        if (newContent != null) {
            oldPost.setContent(newContent);
        }
        if (imgUrl != null) {
            Picture newPic = new Picture(imgUrl);
            oldPost.removeAllPic();
            oldPost.setPicture(newPic);
        }
        if (imgUrl == "") {
            Picture newPic = new Picture("");
            oldPost.removeAllPic();
            oldPost.setPicture(newPic);
        }
        postTracingDAO.createPostTracing(
                buildPostTracingRecord(oldPost, oldPost.getOwnerUser(), PostTracingType.UPDATE_POST));
        postDAO.updatePost(oldPost);
    }

    @Override
    public List<PostResponseDTO> seachPostsByKeywords(String keywords, Long topicId, Long majorId) {
        List<Post> postList = postDAO.seachPostsByKeywords(keywords, topicId, majorId);
        List<PostResponseDTO> results = new ArrayList<>();
        for (Post post : postList) {
            PostResponseDTO responseDTO = new PostResponseDTO(post);
            Set<Picture> pictures = post.getPictures();
            pictures.forEach((pic) -> {
                responseDTO.setPicUrls(pic.getPicUrl());
            });
            results.add(responseDTO);
        }
        return results;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<PostResponseDTO> getAllUserPosts(Long userId) throws UserNotFoundException {

        List<Post> postList;
        List<PostResponseDTO> results = new ArrayList<PostResponseDTO>();
        User user = userDAO.getUserById(userId);
        postList = postDAO.getAllUserPosts(user.getEmail());
        logger.info("GET POSTS FROM USER: " + user.getLoginName());
        for (Post post : postList) {
            PostResponseDTO responseDTO = new PostResponseDTO(post);
            Set<Picture> pictures = post.getPictures();
            pictures.forEach((pic) -> {
                responseDTO.setPicUrls(pic.getPicUrl());
            });
            results.add(responseDTO);
        }
        return results;
    }

    @Override
    public void reactPost(Long id, String email) {
        try {
            Post post = postDAO.getPostById(id);
            User user = userService.getUserByEmail(email);
            userNotificationService.addNotificationForUser(email, NotificationType.NEW_REACTION, post);
            pushNotificationService.sendReactionAddedMessage(post.getOwnerUser(), post.getPostId(), user);
            postDAO.reactPost(id, user);
        } catch (UserNotFoundException | EmptyEntityException e) {
            logger.error("CANNOT REACT POST ", e);
        }
    }

    @Override
    public void removeReactPost(Long id, String email) {
        try {
            User user = userService.getUserByEmail(email);
            postDAO.removeReactPost(id, user);
        } catch (UserNotFoundException e) {
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<UserResponseDTO> getAllPostReation(Long id) throws EmptyEntityException, EmptyReactionException {
        List<UserResponseDTO> result = new ArrayList<UserResponseDTO>();
        Set<User> users = postDAO.getPostById(id).getReactedUser();
        if (users.size() == 0) {
            throw new EmptyReactionException();
        } else {
            users.forEach(user -> {
                UserResponseDTO userRes = new UserResponseDTO(user);
                userRes.setAvatars(avatarDAO.getAllAvatarsByUserId(user.getUserId()));
                result.add(userRes);
            });
        }
        return result;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<CommentResponseDTO> getCommentsByPostId(Long postId)
            throws EmptyCommentException, EmptyEntityException {
        List<Comment> comments = commentDAO.getCommentsByPostId(postId);
        if (comments.size() == 0) {
            throw new EmptyCommentException();
        }
        List<CommentResponseDTO> results = new ArrayList<CommentResponseDTO>();
        comments.forEach((elem) -> {
            CommentResponseDTO cmtDTO = new CommentResponseDTO(elem);
            cmtDTO.setUserAvatarUrl(getUserCurrentActiveAvatarFromComment(elem));
            results.add(cmtDTO);

        });
        return results;

    }

    @Override
    public Long getCountPosts() {
        return postDAO.getCountPosts();
    }

    private String getUserCurrentActiveAvatarFromComment(Comment comment) {
        Iterator<Avatar> avatarIter = Optional.ofNullable(comment)
                .map(Comment::getUser)
                .map(User::getAvatars)
                .orElse(new HashSet<Avatar>())
                .iterator();

        while (avatarIter.hasNext()) {
            Avatar avatar = avatarIter.next();
            if (avatar.getIsActive() == AVATAR_AVTICE) {
                return avatar.getAvaUrl();
            }
        }
        return "";
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private PostTracing buildPostTracingRecord(Post post, User user, PostTracingType eventType) {
        Timestamp createdAt = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        String tracingMessage = buildTracingMessage(user, eventType);
        String tracingPictures = buildPictureUrls(post);
        String postContent;
        switch (eventType) {
            case DELETE_POST:
                postContent = "";
                return new PostTracing(user, post, eventType, tracingMessage, "",
                        postContent, createdAt, -1L, -1L);
            default:
                postContent = post.getContent();
                return new PostTracing(user, post, eventType, tracingMessage, tracingPictures,
                        postContent, createdAt, post.getTopic().getTopicId(), post.getMajor().getMajorId());
        }
    }

    private String buildPictureUrls(Post post) {
        int count = 1;
        String result = "Picture url: ";
        Set<Picture> pictures = post.getPictures();
        if (pictures == null || pictures.size() == 0) {
            return result.concat("[]");
        }
        Iterator<Picture> picIter = pictures.iterator();

        while (picIter.hasNext()) {
            Picture pic = picIter.next();
            result = result.concat(String.format(PICTURE_URL_TEMPLATE, count, pic.getPicUrl()));
            count++;
        }
        return result;
    }

    private String buildTracingMessage(User user, PostTracingType eventType) {
        String userName = user.getGivenName() + " " + user.getFamilyName();
        switch (eventType) {
            case ADD_POST:
                return String.format(ADD_POST_MESSASE_TEMPLATE, userName, user.getEmail());
            case UPDATE_POST:
                return String.format(UPDATE_POST_MESSASE_TEMPLATE, userName, user.getEmail());
            case DELETE_POST:
                return String.format(DELETE_POST_MESSASE_TEMPLATE, userName, user.getEmail());
            default:
                return "CANNOT BUILD TRACING MESSAGE";
        }
    }
}
