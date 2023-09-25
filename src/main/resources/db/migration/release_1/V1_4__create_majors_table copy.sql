CREATE TABLE IF NOT EXISTS PUBLIC.Majors (
    majorId BIGSERIAL NOT NULL,
    majorName VARCHAR(100) NOT NULL,
    majorDescription TEXT NOT NULL,
    followers BIGINT NOT NULL,
    PRIMARY KEY (majorId)
);

INSERT INTO PUBLIC.Majors("majorname", "majordescription", "followers")
VALUES ('General', 'Posts that are related to all majors', 272);

INSERT INTO PUBLIC.Majors("majorname", "majordescription", "followers")
VALUES ('Information Technology', 'Posts that are related to Information Technology major', 272);

INSERT INTO PUBLIC.Majors("majorname", "majordescription", "followers")
VALUES ('Information System', 'Posts that are related to Information System major', 272);

INSERT INTO PUBLIC.Majors("majorname", "majordescription", "followers")
VALUES ('Computer Science', 'Posts that are related to Computer Science major', 272);

INSERT INTO PUBLIC.Majors("majorname", "majordescription", "followers")
VALUES ('Software Engineering', 'Posts that are related to Software Engineering major', 272);

INSERT INTO PUBLIC.Majors("majorname", "majordescription", "followers")
VALUES ('Computer Network and Data Communication', 'Posts that are related to Computer Network and Data Communication major', 272);

INSERT INTO PUBLIC.Majors("majorname", "majordescription", "followers")
VALUES ('Multimedia Communication', 'Posts that are related to Multimedia Communication major', 272);

