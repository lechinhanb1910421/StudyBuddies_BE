CREATE TABLE PUBLIC.Notifications (
    notiId BIGSERIAL NOT NULL,
    sourceUserId BIGINT NOT NULL,
    receiveUserId BIGINT NOT NULL,
    content TEXT,
    notiType VARCHAR(100),
    createdAt TIMESTAMP WITH TIME ZONE,
    referenceLink TEXT,
    PRIMARY KEY (notiId)
);

ALTER TABLE PUBLIC.Notifications
ADD CONSTRAINT receive_user_id_fk FOREIGN KEY (receiveUserId) REFERENCES Users (userId);

