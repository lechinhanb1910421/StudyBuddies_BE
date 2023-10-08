CREATE TABLE PUBLIC.Post_tracing (
    pTracingId BIGSERIAL NOT NULL,
    userId BIGINT NOT NULL,
    userEmail VARCHAR(100),
    postId BIGINT NOT NULL,
    eventType VARCHAR(50),
    message TEXT,
    postContent TEXT,
    pictureUrls TEXT,
    topicId INT,
    majorId INT,
    createdAt TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (ptracingId)
);