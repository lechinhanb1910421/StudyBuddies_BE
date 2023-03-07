CREATE TABLE IF NOT EXISTS PUBLIC.Comments (
    commentId BIGSERIAL NOT NULL,
    postId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    createdTime TIMESTAMP WITH TIME ZONE NOT NULL,
    content TEXT NOT NULL,
    PRIMARY KEY (commentId),
    CONSTRAINT postId FOREIGN KEY (postId) REFERENCES PUBLIC.Posts (postId),
    CONSTRAINT userId FOREIGN KEY (userId) REFERENCES PUBLIC.Users (userId)
);

INSERT INTO public.Comments (postId, userId, createdTime, content)
VALUES (1, 1, '2023-03-04T02:27:01Z', 'The First Comment');

ALTER TABLE PUBLIC.Posts 
ADD COLUMN commentId BIGINT;

ALTER TABLE PUBLIC.Posts 
ADD CONSTRAINT commentId FOREIGN KEY (commentId) REFERENCES PUBLIC.Comments (commentId);