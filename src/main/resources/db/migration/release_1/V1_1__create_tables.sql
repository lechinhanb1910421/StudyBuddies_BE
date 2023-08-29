CREATE TABLE IF NOT EXISTS PUBLIC.posts (
    post_id BIGSERIAL NOT NULL,
    user_id BIGINT,
    created_time TIMESTAMP WITH TIME ZONE,
    content TEXT,
    audience_mode VARCHAR(45),
    PRIMARY KEY (post_id)
);
