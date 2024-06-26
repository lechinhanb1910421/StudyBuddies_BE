package com.everett.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.everett.exceptions.checkedExceptions.MajorNotFoundException;
import com.everett.models.Major;

public class MajorDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public Major getDefaultMajor() {
        return entityManager.find(Major.class, 1l);
    }

    public Major getMajorById(Long id) throws MajorNotFoundException {
        Major major = null;
        try {
            major = entityManager.find(Major.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (major == null) {
            throw new MajorNotFoundException(id);
        } else {
            return major;
        }
    }
}
