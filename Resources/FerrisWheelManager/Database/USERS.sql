CREATE TABLE `USERS` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(128) NOT NULL,
  `role` varchar(20) NOT NULL,
  `salt` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

insert into USERS (username, password, role, salt) values 
('Marvin', 'aed5d790cb9bdb16a89c3c25a0d9b92282e17bf033879898b04922ff34a3cc8cc0b56cefb791edbdce1614ab85cd2bc790cd28b20bdebac6273c4a723d3da17c', 'Manager', 'Marvin');

insert into USERS (username, password, role, salt) values 
('Zaphod', '736addab2f100eb7be2b0494a0316fbc3298b280bd2dc0c8b817654a7193149a625370072f6674f63cc8d58b82f225ed697df26ef6518f60da2900459779cfb0', 'User', 'Zaphod');
