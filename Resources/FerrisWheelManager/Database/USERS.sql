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
('user', '1b6bff8ef91a39daed86f596417c1fb1ce778c32d8ce1208d7709bab120f6c380b4e45d3a4a4b7b2e335fd30fb1c909d1c54deb6409de4246b801b08746167b3', 'User', 'user');

insert into USERS (username, password, role, salt) values 
('manager', '5f9e1231f3c348c7aecd2337dd68443d7216ee768ae38c6e7b944852f3d1eba9de71fea8eefab5dcdb94f1e89793b8ce98a270741f4bba9b81348919bff77e7d', 'Admin', 'manager');

insert into USERS (username, password, role, salt) values 
('Marvin', '490b4124a61f420a29be03b8a2dd0f226e6e16bf3c9371286820b2d8014c36c94e8dcf7e4b2e8fe35c34a48532ec5074f34265f13188d9331239bc9b1e3c528d', 'User', 'Marvin');

insert into USERS (username, password, role, salt) values 
('Zaphod', '736addab2f100eb7be2b0494a0316fbc3298b280bd2dc0c8b817654a7193149a625370072f6674f63cc8d58b82f225ed697df26ef6518f60da2900459779cfb0', 'User', 'Zaphod');
