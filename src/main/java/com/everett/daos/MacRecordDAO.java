package com.everett.daos;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.everett.models.MacRecord;

@Stateless
public class MacRecordDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public List<MacRecord> getRecordsByStackId(String stackId) {
        List<MacRecord> results = new ArrayList<MacRecord>();
        try {
            TypedQuery<MacRecord> macRecordQuery = entityManager.createQuery(
                    "FROM Mac_record mr WHERE mr.stackId = :stackId ORDER BY mr.macRecId",
                    MacRecord.class);
            macRecordQuery.setParameter("stackId", stackId);
            results = macRecordQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public void persistMacRecord(MacRecord macRecord) {
        try {
            entityManager.persist(macRecord);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
