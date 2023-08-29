CREATE TABLE PUBLIC.devices (
    device_id BIGSERIAL NOT NULL,
    user_id BIGINT NOT NULL,
    fcm_token TEXT NOT NULL,
    PRIMARY KEY (device_id)
);

ALTER TABLE PUBLIC.devices
ADD CONSTRAINT user_devices_fk FOREIGN KEY (user_id) REFERENCES users (user_id);
