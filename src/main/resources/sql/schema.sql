/*
DROP TABLE IF EXISTS user_roles;
CREATE  TABLE users (
  userid LONG NOT NULL,
  username VARCHAR(45) NOT NULL ,
  password VARCHAR(60) NOT NULL ,
  enabled BOOLEAN NOT NULL DEFAULT TRUE ,
  PRIMARY KEY (userid));
  
CREATE TABLE user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  userid LONG NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (role,userid),
  CONSTRAINT fk_username FOREIGN KEY (userid) REFERENCES users (userid));
   
  */
  CREATE TABLE persistent_logins (
    username varchar(64) not null,
    series varchar(64) not null,
    token varchar(64) not null,
    last_used timestamp not null,
    PRIMARY KEY (series)
);