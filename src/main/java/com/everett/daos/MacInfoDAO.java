package com.everett.daos;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.everett.models.MacInfo;

@Stateless
public class MacInfoDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public void persistMacInfo(MacInfo macInfo) {
        try {
            entityManager.persist(macInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
