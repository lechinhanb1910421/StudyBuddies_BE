package com.everett.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.exceptions.checkedExceptions.AvatarNotFoundException;
import com.everett.models.Avatar;

public class AvatarDAO {
    private static final Logger logger = LogManager.getLogger(AvatarDAO.class);

    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public Avatar getAvatarsById(Long avaId) {
        logger.info("GET AVATAR ID: " + avaId);

        Avatar avatar = null;
        try {
            avatar = entityManager.find(Avatar.class, avaId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avatar;
    }

    public void removeAvatar(Avatar avatar) {
        logger.info("DELET AVATAR");
        try {
            entityManager.remove(avatar);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeAvatarById(Long avaId) throws AvatarNotFoundException {
        logger.info("DELETE AVATAR BY ID: " + avaId);

        try {
            Avatar avatar = entityManager.find(Avatar.class, avaId);
            entityManager.remove(avatar);
        } catch (Exception e) {
            throw new AvatarNotFoundException(avaId);
        }

    }

    public List<Avatar> getAllAvatarsByUserId(Long userId) {
        List<Avatar> resList = null;
        logger.info("GET ALL AVATAR BY USERID: " + userId);

        try {
            TypedQuery<Avatar> avaQuery = entityManager
                    .createQuery(
                            "FROM Avatars a JOIN FETCH a.user s WHERE s.userId = :userId ORDER BY a.avaId DESC",
                            Avatar.class);
            resList = avaQuery.setParameter("userId", userId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }
}
