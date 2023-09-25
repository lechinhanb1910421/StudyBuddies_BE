CREATE TABLE IF NOT EXISTS PUBLIC.Posts (
    postId BIGSERIAL NOT NULL,
    userId BIGINT,
    createdTime TIMESTAMP WITH TIME ZONE,
    content TEXT,
    audienceMode VARCHAR(45),
    PRIMARY KEY (postId)
);
