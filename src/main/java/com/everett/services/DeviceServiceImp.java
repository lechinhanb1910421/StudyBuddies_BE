package com.everett.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.everett.daos.DeviceDAO;
import com.everett.daos.UserDAO;
import com.everett.exceptions.checkedExceptions.DeviceNotFoundException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Device;
import com.everett.models.User;

@Stateless
public class DeviceServiceImp implements DeviceService {
    @Inject
    UserDAO userDAO;
    @Inject
    DeviceDAO deviceDAO;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addDevice(String email, String token) throws UserNotFoundException {
        User user = userDAO.getUserByEmail(email);
        try {
            deviceDAO.getDeviceByToken(token);
        } catch (DeviceNotFoundException e1) {
            deviceDAO.addDevice(user, token);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceDAO.getAllDevices();
    }

    @Override
    public void deleteDeviceById(Long deviceId) throws DeviceNotFoundException {
        Device device = deviceDAO.getDeviceById(deviceId);
        deviceDAO.deleteDevice(device);
    }
}
