CREATE TABLE IF NOT EXISTS PUBLIC.majors (
    major_id BIGSERIAL NOT NULL,
    major_name VARCHAR(100) NOT NULL,
    major_description TEXT NOT NULL,
    followers BIGINT NOT NULL,
    PRIMARY KEY (major_id)
);

INSERT INTO PUBLIC.majors("major_name", "major_description", "followers")
VALUES ('General', 'Posts that are related to all majors', 272);

INSERT INTO PUBLIC.majors("major_name", "major_description", "followers")
VALUES ('Information Technology', 'Posts that are related to Information Technology major', 272);

INSERT INTO PUBLIC.majors("major_name", "major_description", "followers")
VALUES ('Information System', 'Posts that are related to Information System major', 272);

INSERT INTO PUBLIC.majors("major_name", "major_description", "followers")
VALUES ('Computer Science', 'Posts that are related to Computer Science major', 272);

INSERT INTO PUBLIC.majors("major_name", "major_description", "followers")
VALUES ('Software Engineering', 'Posts that are related to Software Engineering major', 272);

INSERT INTO PUBLIC.majors("major_name", "major_description", "followers")
VALUES ('Computer Network and Data Communication', 'Posts that are related to Computer Network and Data Communication major', 272);

INSERT INTO PUBLIC.majors("major_name", "major_description", "followers")
VALUES ('Multimedia Communication', 'Posts that are related to Multimedia Communication major', 272);

