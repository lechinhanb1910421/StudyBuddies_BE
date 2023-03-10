package com.everett.daos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.everett.exceptions.EmptyCommentException;
import com.everett.models.Comment;

public class CommentDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public void addComment(Comment comment) {
        System.out.println("COMMENT IN DAO: ");
        System.out.println(comment);
        try {
            entityManager.persist(comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Comment> getAllComments() {
        List<Comment> resList = null;
        try {
            TypedQuery<Comment> postQuery = entityManager
                    .createQuery("FROM Comments c ORDER BY c.commentId",
                            Comment.class);
            resList = postQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }

    public List<Comment> getCommentsByPostId(Long postId) throws EmptyCommentException {
        List<Comment> res = null;
        try {
            TypedQuery<Comment> query = entityManager
                    .createQuery("FROM Comments c JOIN FETCH c.post p WHERE p.postId = :postId", Comment.class);
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
}
