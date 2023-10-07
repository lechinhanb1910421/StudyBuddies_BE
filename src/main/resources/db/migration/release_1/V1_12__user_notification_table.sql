CREATE TABLE PUBLIC.Notifications (
    notiId BIGSERIAL NOT NULL,
    receiverId BIGINT NOT NULL,
    content TEXT,
    notiType VARCHAR(100),
    createdAt TIMESTAMP WITH TIME ZONE,
    referenceLink TEXT,
    PRIMARY KEY (notiId)
);

ALTER TABLE PUBLIC.Notifications
ADD CONSTRAINT receiver_user_id_fk FOREIGN KEY (receiverId) REFERENCES Users (userId);

