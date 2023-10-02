CREATE TABLE IF NOT EXISTS PUBLIC.Avatars (
    avaId BIGSERIAL NOT NULL,
    avaUrl TEXT,
    userId BIGINT,
    isActive INT,
    PRIMARY KEY (avaId)
);

ALTER TABLE PUBLIC.Avatars
ADD CONSTRAINT user_id_FK FOREIGN KEY (userId) REFERENCES PUBLIC.Users (userId);

INSERT INTO PUBLIC.Avatars (avaUrl, userId, isActive)
VALUES ('https://firebasestorage.googleapis.com/v0/b/study-buddies-8b16e.appspot.com/o/shared%2Fdefault-user-icon.jpg?alt=media&token=f9f7aae4-858b-4c23-ae99-91dd7b67079e', 1, 1);