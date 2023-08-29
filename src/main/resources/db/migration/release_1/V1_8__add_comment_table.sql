CREATE TABLE IF NOT EXISTS PUBLIC.comments (
    comment_id BIGSERIAL NOT NULL,
    post_id BIGINT,
    user_id BIGINT,
    created_time TIMESTAMP WITH TIME ZONE,
    content TEXT,
    PRIMARY KEY (comment_id),
    CONSTRAINT post_id FOREIGN KEY (post_id) REFERENCES PUBLIC.posts (post_id),
    CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES PUBLIC.users (user_id)
);

INSERT INTO PUBLIC.comments (post_id, user_id, created_time, content)
VALUES (1, 1, '2023-03-04T02:27:01Z', 'The First Comment');

ALTER TABLE PUBLIC.posts 
ADD COLUMN comment_id BIGINT;

ALTER TABLE PUBLIC.posts 
ADD CONSTRAINT comment_id FOREIGN KEY (comment_id) REFERENCES PUBLIC.comments (comment_id);