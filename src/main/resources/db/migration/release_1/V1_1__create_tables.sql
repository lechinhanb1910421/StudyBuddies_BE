CREATE TABLE IF NOT EXISTS PUBLIC.Posts (
    postId BIGSERIAL NOT NULL,
    userId BIGINT NOT NULL,
    createdTime TIMESTAMP WITH TIME ZONE NOT NULL,
    content TEXT NOT NULL,
    audienceMode VARCHAR(45) NOT NULL,
    primary key (postId)
);
