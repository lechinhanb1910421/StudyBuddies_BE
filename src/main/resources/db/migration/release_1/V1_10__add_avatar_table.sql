CREATE TABLE IF NOT EXISTS PUBLIC.avatars (
    ava_id BIGSERIAL NOT NULL,
    ava_url TEXT,
    user_id BIGINT,
    PRIMARY KEY (ava_id)
);

ALTER TABLE PUBLIC.avatars
ADD CONSTRAINT user_id_FK FOREIGN KEY (user_id) REFERENCES PUBLIC.users (user_id);

INSERT INTO PUBLIC.avatars (ava_url, user_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/study-buddies-8b16e.appspot.com/o/b1910421%2Favatars%2Fdefault_user.png?alt=media&token=39c9e8fc-dd4c-41ad-b946-1b50efe416d4', 1);