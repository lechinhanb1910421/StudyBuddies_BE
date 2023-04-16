package com.everett.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.everett.daos.CommentDAO;
import com.everett.dtos.CommentResponseDTO;
import com.everett.exceptions.checkedExceptions.CommentNotFoundException;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Comment;
import com.everett.models.Message;
import com.everett.models.Post;
import com.everett.models.User;

@Stateless
public class CommentServiceImp implements CommentService {

    @Inject
    PostService postService;

    @Inject
    UserService userService;

    @Inject
    CommentDAO commentDAO;

    @Inject
    NotificationService notificationService;

    @Override
    public void addComment(Long postId, String content, String email) {
        try {
            Post post = postService.getPostById(postId);
            User commenterUser = userService.getUserByEmail(email);
            Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
            Comment comment = new Comment(post, commenterUser, createdTime, content);
            commentDAO.addComment(comment);
           
            notificationService.sendCommentAddedMessage(post.getOwnerUser(), postId, commenterUser);
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
    public void deleteComment(Long cmtId) throws CommentNotFoundException {
        commentDAO.deleteComment(cmtId);

    }

}
