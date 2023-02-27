CREATE TABLE IF NOT EXISTS PUBLIC.Topics (
    topicId BIGSERIAL NOT NULL,
    topicName VARCHAR(100) NOT NULL,
    topicDescription TEXT NOT NULL,
    followers BIGINT NOT NULL,
    primary key (topicId)
);

INSERT INTO PUBLIC.Topics ("topicname", "topicdescription", "followers")
VALUES ('General', 'Posts that have no specific topics', 0);

INSERT INTO PUBLIC.Topics ("topicname", "topicdescription", "followers")
VALUES ('Recreation', 'Recreation posts', 10);

INSERT INTO PUBLIC.Topics ("topicname", "topicdescription", "followers")
VALUES ('Knowleage Share', 'Posts in which we want to share information', 20);

INSERT INTO PUBLIC.Topics ("topicname", "topicdescription", "followers")
VALUES ('Experience', 'Posts in which we share our experiences on something', 30);

INSERT INTO PUBLIC.Topics ("topicname", "topicdescription", "followers")
VALUES ('Help', 'Posts in which we want to ask for help from the community', 40);

INSERT INTO PUBLIC.Topics ("topicname", "topicdescription", "followers")
VALUES ('Sale', 'Posts in which we want sale study gadgets to others', 50);

SELECT * FROM PUBLIC.Topics;
