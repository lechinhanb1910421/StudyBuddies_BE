CREATE TABLE IF NOT EXISTS PUBLIC.Posts (
    postId BIGSERIAL NOT NULL,
    userId BIGINT NOT NULL,
    createdTime TIMESTAMP WITH TIME ZONE NOT NULL,
    content TEXT NOT NULL,
    audienceMode VARCHAR(45) NOT NULL,
    primary key (postId)
);

CREATE TABLE IF NOT EXISTS PUBLIC.Users (
    userId BIGSERIAL NOT NULL,
    userName VARCHAR(45) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password TEXT NOT NULL,
    dateRegistered TIMESTAMP WITH TIME ZONE NOT NULL,
    userType VARCHAR(45) NOT NULL,
    accountStatus VARCHAR(45) NOT NULL,
    primary key (userId)
);

ALTER TABLE PUBLIC.Users ADD CONSTRAINT 
user_id_fk FOREIGN KEY (userId) REFERENCES PUBLIC.Users (userId) ON DELETE CASCADE;

INSERT INTO PUBLIC.Users ("username", "email", "password", "dateregistered", "usertype", "accountstatus")
VALUES ('admin', 'admin@example.com','admin', '2023-02-04T02:07:01', 'admin', 'online');

INSERT INTO PUBLIC.Posts("userid", "createdtime", "content", "audiencemode")
VALUES (1,'2023-02-04T02:07:01', 'The First Post', 'public');

SELECT * FROM PUBLIC.Posts;

SELECT * FROM PUBLIC.Users;