package com.everett.services;

import java.util.List;

import com.everett.exceptions.checkedExceptions.DeviceNotFoundException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Device;

public interface DeviceService {
    public void addDevice(String email, String token) throws UserNotFoundException;

    public void deleteDeviceById(Long deviceId) throws DeviceNotFoundException;

    public List<Device> getAllDevices();
}
