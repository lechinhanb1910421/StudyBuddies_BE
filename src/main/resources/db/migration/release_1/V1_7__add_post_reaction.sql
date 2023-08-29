CREATE TABLE IF NOT EXISTS PUBLIC.post_reaction (
    post_id BIGINT NOT NULL,
    user_id BIGINT,
    PRIMARY KEY (post_id, user_id),
    CONSTRAINT post_id FOREIGN KEY (post_id) REFERENCES PUBLIC.posts (post_id),
    CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES PUBLIC.users (user_id)
);

INSERT INTO PUBLIC.post_reaction(post_id, user_id)
VALUES (1, 1);