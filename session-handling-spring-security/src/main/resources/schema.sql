CREATE TABLE users (
  username VARCHAR(45) NOT NULL,
  password VARCHAR(60) NOT NULL,
  enabled TINYINT NOT NULL,
  PRIMARY KEY (username));

CREATE TABLE authorities (
  id INTEGER NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL,
  authority VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));
