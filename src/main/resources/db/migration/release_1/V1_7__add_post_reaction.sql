CREATE TABLE IF NOT EXISTS PUBLIC.post_reaction (
    postId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    primary key (postId, userId),
    CONSTRAINT postId FOREIGN KEY (postId) REFERENCES PUBLIC.Posts (postId),
    CONSTRAINT userId FOREIGN KEY (userId) REFERENCES PUBLIC.Users (userId)
);

INSERT INTO PUBLIC.post_reaction(postId, userId)
VALUES (1, 1);

SELECT pr FROM PUBLIC.post_reaction pr;