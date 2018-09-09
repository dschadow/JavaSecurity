INSERT INTO users(username, password, enabled)
  VALUES ('user','$2a$10$uyw4NHXu52GKyc2iJRfyOu/p.jn2IXhibpvYEAO4AXcaTQ0LXBCnq', 1);

INSERT INTO users(username, password, enabled)
  VALUES ('admin','$2a$10$7N00PGwYhJ1GT/8zf0KZD.wZhSbFDhs49HEx7wOkORu3q0/zhqyWe', 1);

INSERT INTO authorities (username, authority)
  VALUES ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority)
  VALUES ('admin', 'ROLE_ADMIN');

