SELECT * FROM PUBLIC.Posts;

SELECT * FROM PUBLIC.Topics;

SELECT * FROM PUBLIC.Posts p
INNER JOIN PUBLIC.Topics t ON p.topicid = t.topicid;

SELECT * FROM PUBLIC.Majors;
