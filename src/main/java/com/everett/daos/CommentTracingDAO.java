package com.everett.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.models.CommentTracing;

public class CommentTracingDAO {
    private static final Logger logger = LogManager.getLogger(CommentTracingDAO.class);

    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public void createCommentTracing(CommentTracing commentTracing) {
        try {
            logger.info("PERSIST COMMENT TRACING RECORD");
            entityManager.persist(commentTracing);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
