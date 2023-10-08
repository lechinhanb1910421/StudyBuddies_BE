CREATE TABLE IF NOT EXISTS PUBLIC.Pictures (
    picId BIGSERIAL NOT NULL,
    picUrl TEXT,
    postId BIGINT,
    PRIMARY KEY (picId)
);

INSERT INTO PUBLIC.Pictures (picUrl, postId)
VALUES ('../pics/Ganyu_1.jpeg', 1);

ALTER TABLE PUBLIC.Pictures 
ADD CONSTRAINT postId FOREIGN KEY (postId) REFERENCES PUBLIC.Posts (postId);