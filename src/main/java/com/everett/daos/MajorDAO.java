package com.everett.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.everett.exceptions.MajorNotFoundException;
import com.everett.models.Major;

public class MajorDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public Major getDefaultMajor() {
        return entityManager.find(Major.class, 1l);
    }

    public Major getMajorById(Long id) throws MajorNotFoundException {
        try {
            return entityManager.find(Major.class, id);
        } catch (Exception e) {
            throw new MajorNotFoundException(id);
        }
    }
}
