package com.everett.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.everett.exceptions.checkedExceptions.CommentNotFoundException;
import com.everett.exceptions.checkedExceptions.EmptyCommentException;
import com.everett.models.Comment;

public class CommentDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public Comment getCommentById(Long id) throws CommentNotFoundException {
        Comment cmt = entityManager.find(Comment.class, id);
        if (cmt == null) {
            throw new CommentNotFoundException();
        }
        return cmt;
    }

    public void addComment(Comment comment) {
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
                    .createQuery("FROM Comments c ORDER BY c.commentId", Comment.class);
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
                    .createQuery("FROM Comments c WHERE c.post.postId = :postId", Comment.class);
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

    public void deleteComment(Long cmtId) throws CommentNotFoundException {
        Comment comment = entityManager.find(Comment.class, cmtId);
        if (comment == null) {
            throw new CommentNotFoundException();
        } else {
            entityManager.remove(comment);
        }
    }
}
