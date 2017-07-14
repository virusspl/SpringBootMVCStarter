INSERT INTO users(userid,username,password,enabled)
VALUES (1,'Admin','$2a$04$UZWCi1I779DTvZzdYI/YG.oRidHjWNsxQcW9I7QqapOrYE8tXelu6', true);
INSERT INTO users(userid,username,password,enabled)
VALUES (2,'User','$2a$04$VYg7rxx7ZKLszJbLxAW48eMu/cYy8Asg4BFmOkEawwK6WfuywISdS', true);

INSERT INTO user_roles (userid, role)
VALUES (2L, 'ROLE_USER');
INSERT INTO user_roles (userid, role)
VALUES (1L, 'ROLE_ADMIN');
INSERT INTO user_roles (userid, role)
VALUES (1L, 'ROLE_USER');