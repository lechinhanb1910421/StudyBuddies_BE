CREATE TABLE PUBLIC.Post_tracing (
    pTracingId BIGSERIAL NOT NULL,
    userId BIGINT NOT NULL,
    postId BIGINT NOT NULL,
    eventType VARCHAR(50),
    message TEXT,
    postContent TEXT,
    pictureUrls TEXT,
    createdAt TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (ptracingId)
);

ALTER TABLE PUBLIC.Post_tracing
ADD CONSTRAINT ptracing_user_id_fk FOREIGN KEY (userId) REFERENCES Users (userId);

