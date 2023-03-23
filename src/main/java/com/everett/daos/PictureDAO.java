package com.everett.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.models.Picture;

public class PictureDAO {
    private static final Logger logger = LogManager.getLogger(PictureDAO.class);
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public Picture getPictureById(Long id) throws EmptyEntityException {
        logger.info("GET PICTURE BY ID: " + id);
        Picture picture = entityManager.find(Picture.class, id);
        if (picture == null) {
            throw new EmptyEntityException(id);
        } else {
            return picture;
        }
    }

    public List<Picture> getAllPictures() {
        List<Picture> resList = null;
        logger.info("GET ALL PICTURES");
        try {
            TypedQuery<Picture> picQuery = entityManager.createQuery(
                    "FROM Pictures pic ORDER BY pic.picId", Picture.class);
            resList = picQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }

    public void addPicture(Picture picture) {
        logger.info("ADD PICTURE");
        entityManager.persist(picture);
    }
}
