CREATE TABLE IF NOT EXISTS PUBLIC.Users (
    userId BIGSERIAL NOT NULL,
    loginName VARCHAR(27),
    givenName VARCHAR(20),
    familyName VARCHAR(27),
    email VARCHAR(80),
    createdDate TIMESTAMP WITH TIME ZONE,
    accountStatus VARCHAR(27) DEFAULT 'active',
    PRIMARY KEY (userId)
);
ALTER TABLE PUBLIC.Posts 
DROP COLUMN userId;

ALTER TABLE PUBLIC.Posts
ADD COLUMN userId BIGINT NOT NULL;

INSERT INTO PUBLIC.Posts(userId, createdTime, content, audienceMode)
VALUES (1, '2023-03-03T02:27:01Z', 'First Post', 'public');

INSERT INTO PUBLIC.Users (loginName, givenName, familyName, email, createdDate, accountStatus)
VALUES ('b1910421', 'Nhan', 'Le Nguyen Chi', 'nhanb1910421@student.ctu.edu.vn', '2023-02-27T02:27:01Z', 'active');

ALTER TABLE PUBLIC.Posts ADD CONSTRAINT 
user_post_id_fk FOREIGN KEY (userId) REFERENCES PUBLIC.Users (userId);