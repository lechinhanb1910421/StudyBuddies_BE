CREATE TABLE PUBLIC.Devices (
    deviceId BIGSERIAL NOT NULL,
    userId BIGINT NOT NULL,
    fcmToken TEXT NOT NULL,
    PRIMARY KEY (deviceId)
);

ALTER TABLE PUBLIC.Devices
ADD CONSTRAINT user_devices_fk FOREIGN KEY (userId) REFERENCES Users (userId);
