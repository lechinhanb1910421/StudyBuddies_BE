CREATE TABLE IF NOT EXISTS PUBLIC.Users (
    userId BIGSERIAL NOT NULL,
    loginName VARCHAR(27) NOT NULL,
    givenName VARCHAR(20) NOT NULL,
    familyName VARCHAR(27) NOT NULL,
    email VARCHAR(80) NOT NULL,
    createdDate TIMESTAMP WITH TIME ZONE NOT NULL,
    accountStatus VARCHAR(27) NOT NULL DEFAULT 'active',
    primary key (userId)
);
ALTER TABLE PUBLIC.Posts 
DROP COLUMN userId;

ALTER TABLE PUBLIC.Posts
ADD COLUMN userId BIGINT NOT NULL;

INSERT INTO PUBLIC.Users (loginName, givenName, familyName, email, createdDate, accountStatus)
VALUES ('admin', 'admin', 'ad', 'admin@example.com', '2022-12-22T07:27:01+0700', 'testing');

ALTER TABLE PUBLIC.Posts ADD CONSTRAINT 
user_post_id_fk FOREIGN KEY (userId) REFERENCES PUBLIC.Users (userId);