package com.everett.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.exceptions.checkedExceptions.CommentNotFoundException;
import com.everett.exceptions.checkedExceptions.EmptyCommentException;
import com.everett.models.Comment;

public class CommentDAO {
    private static final Logger logger = LogManager.getLogger(CommentDAO.class);

    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public Comment getCommentById(Long id) throws CommentNotFoundException {
        logger.info("GET COMMENT WITH ID: " + id);
        Comment cmt = entityManager.find(Comment.class, id);
        if (cmt == null) {
            throw new CommentNotFoundException();
        }
        return cmt;
    }

    public void addComment(Comment comment) {
        logger.info("ADD COMMENT TO DB");
        try {
            entityManager.persist(comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Comment> getAllComments() {
        logger.info("GET ALL COMMENTS");
        List<Comment> resList = null;
        try {
            TypedQuery<Comment> postQuery = entityManager
                    .createQuery("FROM Comments c JOIN FETCH c.user s ORDER BY c.commentId DESC", Comment.class);
            resList = postQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }

    public List<Comment> getCommentsByPostId(Long postId) throws EmptyCommentException {
        logger.info("GET ALL COMMENTS FROM POST ID: " + postId);

        List<Comment> res = null;
        try {
            TypedQuery<Comment> query = entityManager
                    .createQuery(
                            "FROM Comments c JOIN FETCH c.user s WHERE c.post.postId = :postId ORDER BY c.commentId DESC",
                            Comment.class);
            res = query.setParameter("postId", postId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res == null) {
            throw new EmptyCommentException();
        } else {
            return res;
        }
    }

    public void deleteComment(Comment comment) {
        logger.info("DELETE CMT WITH ID: " + comment.getCommentId());
        entityManager.remove(comment);
    }
}
