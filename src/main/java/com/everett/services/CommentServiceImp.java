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
import javax.ws.rs.core.SecurityContext;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;

import com.everett.daos.CommentDAO;
import com.everett.dtos.CommentResponseDTO;
import com.everett.exceptions.UserNotFoundException;
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

    @Override
    @SuppressWarnings("rawtypes")
    public void addComment(Long postId, String content, SecurityContext context) {
        try {
            Post post = postService.getPostById(postId);
            System.out.println("POST COMMENT IN SERVICE");
            System.out.println(post.getPostId() + " " + post.getContent());

            KeycloakPrincipal principal = (KeycloakPrincipal) context.getUserPrincipal();
            AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();
            User user = userService.getUserByEmail(accessToken.getEmail());
            System.out.println("USER IN SERVICE ");
            System.out.println(user);

            Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));

            Comment comment = new Comment(post, user, createdTime, content);
            commentDAO.addComment(comment);
        } catch (UserNotFoundException e) {
            Message msg = new Message("User with id does not exist");
            throw new WebApplicationException(Response.status(400).entity(msg).build());
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

}
