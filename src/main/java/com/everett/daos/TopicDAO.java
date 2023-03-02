package com.everett.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.everett.exceptions.TopicNotFoundException;
import com.everett.models.Topic;

public class TopicDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public Topic getDefaultTopic() {
        return entityManager.find(Topic.class, 1l);
    }

    public Topic getTopicById(Long id) throws TopicNotFoundException {
        Topic topic = null;
        try {
            topic = entityManager.find(Topic.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (topic == null) {
            throw new TopicNotFoundException(id);
        } else {
            return topic;
        }
    }
}
