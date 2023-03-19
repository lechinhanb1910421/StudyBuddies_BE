CREATE TABLE IF NOT EXISTS PUBLIC.Avatars (
    avaId BIGSERIAL NOT NULL,
    avaUrl TEXT NOT NULL,
    userId BIGINT NOT NULL,
    PRIMARY KEY (avaId)
);

INSERT INTO public.Avatars (avaUrl, userId)
VALUES ('https://firebasestorage.googleapis.com/v0/b/study-buddies-8b16e.appspot.com/o/b1910421%2Favatars%2Fdefault_user.png?alt=media&token=39c9e8fc-dd4c-41ad-b946-1b50efe416d4', 1);

INSERT INTO public.Avatars (avaUrl, userId)
VALUES ('https://firebasestorage.googleapis.com/v0/b/study-buddies-8b16e.appspot.com/o/b1910421%2Favatars%2Fdefault_user.png?alt=media&token=39c9e8fc-dd4c-41ad-b946-1b50efe416d4', 2);

ALTER TABLE PUBLIC.Avatars 
ADD CONSTRAINT userId FOREIGN KEY (userId) REFERENCES PUBLIC.Users (userId);

SELECT * FROM PUBLIC.Users s JOIN PUBLIC.Avatars a ON a.userId = s.userId;