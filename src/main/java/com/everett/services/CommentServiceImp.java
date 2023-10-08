package com.everett.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.daos.CommentDAO;
import com.everett.daos.CommentTracingDAO;
import com.everett.dtos.CommentResponseDTO;
import com.everett.exceptions.checkedExceptions.CommentNotFoundException;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Comment;
import com.everett.models.CommentTracing;
import com.everett.models.Message;
import com.everett.models.Post;
import com.everett.models.User;
import com.everett.models.type.CommentTracingType;
import com.everett.models.type.NotificationType;

@Stateless
public class CommentServiceImp implements CommentService {
    private static final String ADD_COMMENT_TRACING_TEMPLATE = "User [%s] with email [%s] add a comment";
    private static final String DELETE_COMMENT_TRACING_TEMPLATE = "User [%s] with email [%s] delete a comment";
    private static final Logger logger = LogManager.getLogger(CommentServiceImp.class);

    @Inject
    PostService postService;

    @Inject
    UserService userService;

    @Inject
    CommentDAO commentDAO;

    @Inject
    PushNotificationService pushNotificationService;

    @Inject
    UserNotificationService userNotificationService;

    @Inject
    CommentTracingDAO commentTracingDAO;

    @Override
    public void addComment(Long postId, String content, String email) {
        try {
            logger.info(String.format("ADD COMMENT TO POST ID [%s] FOR USER [%s]", postId, email));
            Post post = postService.getPostById(postId);
            User commenterUser = userService.getUserByEmail(email);
            Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
            Comment comment = new Comment(post, commenterUser, createdTime, content);
            commentDAO.addComment(comment);
            CommentTracing commentTracing = buildCommentTracingRecord(commenterUser,
                    post, comment,
                    CommentTracingType.ADD_COMMENT);
            commentTracingDAO.createCommentTracing(commentTracing);
            userNotificationService.addNotificationForUser(email,
                    NotificationType.NEW_COMMENT, post);
            pushNotificationService.sendCommentAddedMessage(post.getOwnerUser(), postId,
                    commenterUser);
        } catch (UserNotFoundException e) {
            Message msg = new Message("User with email " + email + " does not exist");
            throw new WebApplicationException(Response.status(400).entity(msg).build());
        } catch (EmptyEntityException e) {
            Message msg = new Message("Post with id:" + postId + " does not exist");
            throw new WebApplicationException(Response.status(400).entity(msg).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CommentResponseDTO> getAllComments() {
        List<Comment> comments = commentDAO.getAllComments();
        List<CommentResponseDTO> results = new ArrayList<CommentResponseDTO>();
        comments.forEach(elememt -> {
            results.add(new CommentResponseDTO(elememt));
        });
        return results;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteComment(Long cmtId) throws CommentNotFoundException {
        Comment comment = commentDAO.getCommentById(cmtId);
        commentTracingDAO.createCommentTracing(
                buildCommentTracingRecord(comment.getUser(), comment.getPost(), comment,
                        CommentTracingType.DELETE_COMMENT));
        commentDAO.deleteComment(comment);
    }

    private CommentTracing buildCommentTracingRecord(User user, Post post, Comment comment,
            CommentTracingType eventType) {
        Timestamp createdAt = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        String message = buildTracingMessage(eventType, user);
        String commentMessage = eventType == CommentTracingType.DELETE_COMMENT ? "" : comment.getContent();
        return new CommentTracing(user.getUserId(), user.getEmail(), post.getPostId(), eventType, message,
                commentMessage, createdAt);

    }

    private String buildTracingMessage(CommentTracingType eventType, User user) {
        String userName = user.getGivenName() + " " + user.getFamilyName();
        switch (eventType) {
            case ADD_COMMENT:
                return String.format(ADD_COMMENT_TRACING_TEMPLATE, userName, user.getEmail());
            case DELETE_COMMENT:
                return String.format(DELETE_COMMENT_TRACING_TEMPLATE, userName, user.getEmail());
            default:
                return "CANNOT BUILD COMMENT TRACING MESSAGE";
        }
    }

}
