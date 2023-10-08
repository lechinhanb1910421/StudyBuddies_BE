package com.everett.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.models.PostTracing;

public class PostTracingDAO {
    private static final Logger logger = LogManager.getLogger(PostTracingDAO.class);

    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public void createPostTracing(PostTracing postTracing) {
        try {
            logger.info("PERSIST POST TRACING RECORD");
            entityManager.persist(postTracing);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
