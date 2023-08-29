CREATE TABLE IF NOT EXISTS PUBLIC.pictures (
    pic_id BIGSERIAL NOT NULL,
    pic_url TEXT,
    post_id BIGINT,
    PRIMARY KEY (pic_id)
);

INSERT INTO PUBLIC.pictures (pic_url, post_id)
VALUES ('../assets/Ganyu_2.jpeg', 1);

ALTER TABLE PUBLIC.pictures 
ADD CONSTRAINT post_id FOREIGN KEY (post_id) REFERENCES PUBLIC.posts (post_id);