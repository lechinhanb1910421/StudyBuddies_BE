CREATE TABLE IF NOT EXISTS PUBLIC.Post_reaction (
    postId BIGINT NOT NULL,
    userId BIGINT,
    PRIMARY KEY (postId, userId),
    CONSTRAINT postId FOREIGN KEY (postId) REFERENCES PUBLIC.posts (postId),
    CONSTRAINT userId FOREIGN KEY (userId) REFERENCES PUBLIC.users (userId)
);

INSERT INTO PUBLIC.post_reaction(postId, userId)
VALUES (1, 1);