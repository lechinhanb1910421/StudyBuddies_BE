CREATE TABLE IF NOT EXISTS PUBLIC.topics (
    topic_id BIGSERIAL NOT NULL,
    topic_name VARCHAR(100),
    topic_description TEXT,
    followers BIGINT,
    PRIMARY KEY (topic_id)
);

INSERT INTO PUBLIC.topics ("topic_name", "topic_description", "followers")
VALUES ('General', 'Posts that have no specific topics', 0);

INSERT INTO PUBLIC.topics ("topic_name", "topic_description", "followers")
VALUES ('Recreation', 'Recreation posts', 10);

INSERT INTO PUBLIC.topics ("topic_name", "topic_description", "followers")
VALUES ('Knowleage Share', 'Posts in which we want to share information', 20);

INSERT INTO PUBLIC.topics ("topic_name", "topic_description", "followers")
VALUES ('Experience', 'Posts in which we share our experiences on something', 30);

INSERT INTO PUBLIC.topics ("topic_name", "topic_description", "followers")
VALUES ('Help', 'Posts in which we want to ask for help from the community', 40);

INSERT INTO PUBLIC.topics ("topic_name", "topic_description", "followers")
VALUES ('Sale', 'Posts in which we want sale study gadgets to others', 50);

SELECT * FROM PUBLIC.topics;
