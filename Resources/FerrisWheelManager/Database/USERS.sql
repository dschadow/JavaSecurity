CREATE TABLE `USERS` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

insert into USERS (username, password, role) values 
('user', 'ferris', 'User');

insert into USERS (username, password, role) values 
('manager', 'wheel', 'Admin');

insert into USERS (username, password, role) values 
('Marvin', 'ferris', 'User');

insert into USERS (username, password, role) values 
('Zaphod', 'ferris', 'User');
