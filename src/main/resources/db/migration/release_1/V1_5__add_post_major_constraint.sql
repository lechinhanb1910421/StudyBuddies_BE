ALTER TABLE PUBLIC.posts
ADD COLUMN "major_id" INT NOT NULL DEFAULT 1;

ALTER TABLE PUBLIC.posts
ADD CONSTRAINT major_id_fk FOREIGN KEY (major_id) REFERENCES majors (major_id);