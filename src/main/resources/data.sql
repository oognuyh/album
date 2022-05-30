-- USER
INSERT INTO user(user_id, name, nickname, email, password, created_at, modified_at) VALUES('9b6ed57eb61f4e0db4d2eb16005ac698', 'tester', 'tester', 'tester@test.org', '$2a$10$PN5HKqeGSOJ.OxZKC9e.Aurgtr.0qaVDE5J2cMc5ozWfAcI8abxKy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ROLE
INSERT INTO role(user_id, name) VALUES('9b6ed57eb61f4e0db4d2eb16005ac698', 'USER');

-- POST
INSERT INTO post(post_id, title, content, author_id, created_at, modified_at) VALUES('1d471356419a4c3f8e8156a717a112a2', 'title', 'description', '9b6ed57eb61f4e0db4d2eb16005ac698', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- IMAGE
INSERT INTO image(post_id, url) VALUES('1d471356419a4c3f8e8156a717a112a2', 'https://oognuyh.asuscomm.com/api/v1/images/imageId');

-- TAG
INSERT INTO tag(post_id, name) VALUES('1d471356419a4c3f8e8156a717a112a2', 'tag01');

-- COMMENT
INSERT INTO comment(comment_id, content, author_id, post_id, created_at, modified_at) VALUES('5562d5a30beb4f528f48779c488038c1', 'content', '9b6ed57eb61f4e0db4d2eb16005ac698', '1d471356419a4c3f8e8156a717a112a2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);