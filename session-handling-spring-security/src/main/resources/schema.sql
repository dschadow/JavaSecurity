CREATE TABLE users (
  username VARCHAR(45) NOT NULL,
  password VARCHAR(60) NOT NULL,
  active TINYINT NOT NULL,
  PRIMARY KEY (username));

CREATE TABLE roles (
  role_id INTEGER NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL,
  role VARCHAR(45) NOT NULL,
  PRIMARY KEY (role_id),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));
