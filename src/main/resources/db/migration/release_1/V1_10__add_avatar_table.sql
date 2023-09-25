CREATE TABLE IF NOT EXISTS PUBLIC.Avatars (
    avaId BIGSERIAL NOT NULL,
    avaUrl TEXT,
    userId BIGINT,
    isActive BOOLEAN,
    PRIMARY KEY (avaId)
);

ALTER TABLE PUBLIC.Avatars
ADD CONSTRAINT user_id_FK FOREIGN KEY (userId) REFERENCES PUBLIC.Users (userId);

INSERT INTO PUBLIC.Avatars (avaUrl, userId, isActive)
VALUES ('https://firebasestorage.googleapis.com/v0/b/study-buddies-8b16e.appspot.com/o/b1910421%2Favatars%2Fdefault_user.png?alt=media&token=39c9e8fc-dd4c-41ad-b946-1b50efe416d4', 1, true);