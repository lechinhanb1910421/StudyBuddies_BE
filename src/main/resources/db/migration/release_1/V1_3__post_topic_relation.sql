ALTER TABLE PUBLIC.Posts
ADD COLUMN "topicid" INT NOT NULL DEFAULT 1;

ALTER TABLE PUBLIC.Posts
ADD CONSTRAINT topic_id_fk FOREIGN KEY (topicid) REFERENCES Topics (topicid);

-- INSERT INTO PUBLIC.Posts("userid", "createdtime", "content", "audiencemode", "topicid")
-- VALUES (1,'2023-02-04T02:07:01', 'The First Post', 'public', 1);

SELECT * FROM PUBLIC.Posts;

SELECT * FROM PUBLIC.Topics;

SELECT * FROM PUBLIC.Posts p
INNER JOIN PUBLIC.Topics t ON p.topicid = t.topicid;
