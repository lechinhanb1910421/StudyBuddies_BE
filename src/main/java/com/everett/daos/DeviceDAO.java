package com.everett.daos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.everett.exceptions.checkedExceptions.DeviceNotFoundException;
import com.everett.models.Device;
import com.everett.models.User;

@Stateless
public class DeviceDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public List<Device> getAllDevices() {
        List<Device> list = null;
        try {
            TypedQuery<Device> query = entityManager.createQuery("FROM Devices d ORDER by d.deviceId", Device.class);
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Device getDeviceById(Long deviceId) throws DeviceNotFoundException {
        Device device = entityManager.find(Device.class, deviceId);
        if (device == null) {
            throw new DeviceNotFoundException();
        }
        return device;
    }

    public Device addDevice(User user, String token) {
        Device device = new Device(user, token);
        try {
            entityManager.persist(device);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return device;
    }

    public Device getDeviceByToken(String token) throws DeviceNotFoundException {
        Device device = null;
        try {
            TypedQuery<Device> query = entityManager.createQuery("FROM Devices d WHERE d.fcmToken = :fcmToken",
                    Device.class);
            device = query.setParameter("fcmToken", token).getSingleResult();
        } catch (NoResultException e) {
            throw new DeviceNotFoundException();
        }
        return device;
    }

    public void deleteDeviceByToken(String token) throws DeviceNotFoundException {
        Device device = getDeviceByToken(token);
        try {
            entityManager.remove(device);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteDevice(Device device) {
        try {
            entityManager.remove(device);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
