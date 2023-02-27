ALTER TABLE PUBLIC.Posts
ADD COLUMN "majorid" INT NOT NULL DEFAULT 1;

ALTER TABLE PUBLIC.Posts
ADD CONSTRAINT major_id_fk FOREIGN KEY (majorid) REFERENCES Majors (majorid);

INSERT INTO PUBLIC.Posts("userid", "createdtime", "content", "audiencemode", "topicid", "majorid")
VALUES (1,'2023-02-04T02:07:01', 'The First Post', 'public', 1, 1);

SELECT * FROM PUBLIC.Posts;

SELECT * FROM PUBLIC.Topics;

SELECT * FROM PUBLIC.Posts p
INNER JOIN PUBLIC.Topics t ON p.topicid = t.topicid;

SELECT * FROM PUBLIC.Posts p
INNER JOIN PUBLIC.Majors m ON p.majorid = m.majorid;
