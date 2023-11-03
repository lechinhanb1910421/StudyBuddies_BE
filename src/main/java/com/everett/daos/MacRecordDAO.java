package com.everett.daos;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.everett.models.MacRecord;

@Stateless
public class MacRecordDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public void persistMacRecord(MacRecord macRecord) {
        try {
            entityManager.persist(macRecord);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
