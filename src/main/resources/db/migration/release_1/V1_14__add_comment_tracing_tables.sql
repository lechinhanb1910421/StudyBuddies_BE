CREATE TABLE PUBLIC.Comment_tracing (
    cTracingId BIGSERIAL NOT NULL,
    userId BIGINT NOT NULL,
    userEmail VARCHAR(100),
    postId BIGINT NOT NULL,
    eventType VARCHAR(50),
    message TEXT,
    commentContent TEXT,
    createdAt TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (cTracingId)
);