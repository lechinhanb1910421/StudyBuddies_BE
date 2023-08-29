CREATE TABLE IF NOT EXISTS PUBLIC.users (
    user_id BIGSERIAL NOT NULL,
    login_name VARCHAR(27),
    given_name VARCHAR(20),
    family_name VARCHAR(27),
    email VARCHAR(80),
    created_date TIMESTAMP WITH TIME ZONE,
    account_status VARCHAR(27) DEFAULT 'active',
    PRIMARY KEY (user_id)
);
ALTER TABLE PUBLIC.posts 
DROP COLUMN user_id;

ALTER TABLE PUBLIC.posts
ADD COLUMN user_id BIGINT NOT NULL;

INSERT INTO PUBLIC.posts(user_id, created_time, content, audience_mode)
VALUES (1, '2023-03-03T02:27:01Z', 'First Post', 'public');

INSERT INTO PUBLIC.users (login_name, given_name, family_name, email, created_date, account_status)
VALUES ('b1910421', 'Nhan', 'Le Nguyen Chi', 'nhanb1910421@student.ctu.edu.vn', '2023-02-27T02:27:01Z', 'active');

ALTER TABLE PUBLIC.posts ADD CONSTRAINT 
user_post_id_fk FOREIGN KEY (user_id) REFERENCES PUBLIC.users (user_id);