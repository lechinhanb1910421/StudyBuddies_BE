package com.everett.utils;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Startup
@Singleton
@DependsOn("MigrationAPI")
public class InitHibernateSearch {
    private static final Logger logger = LogManager.getLogger(InitHibernateSearch.class);

    @PersistenceContext
    EntityManager entityManager;

    @PostConstruct
    public void init() {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
            System.out.println("================Indexing successfully");
            logger.info("================Indexing successfully");
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }
}
