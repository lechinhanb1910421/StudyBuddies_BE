CREATE TABLE IF NOT EXISTS PUBLIC.Pictures (
    picId BIGSERIAL NOT NULL,
    picUrl TEXT NOT NULL,
    postId BIGINT NOT NULL,
    PRIMARY KEY (picId)
);

INSERT INTO public.Pictures (picUrl, postId)
VALUES ('../assets/Ganyu_2.jpeg', 1);

ALTER TABLE PUBLIC.Pictures 
ADD CONSTRAINT postId FOREIGN KEY (postId) REFERENCES PUBLIC.Posts (postId);