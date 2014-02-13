CREATE TABLE `FERRIS_WHEELS` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `location` varchar(50) NOT NULL,
  `speed` int(4) DEFAULT 0,
  `installation_date` date DEFAULT NULL,
  `maintenance_date` date DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  CONSTRAINT `ferris_wheel_owner` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


insert into ferris_wheels (name, description, location, speed,
installation_date, maintenance_date, user_id)
values
('Small One', 'The small one turning fast', 'Lake', 125, '2000-01-01', '2014-01-01', (select id from users where username = 'Marvin'));

insert into ferris_wheels (name, description, location, speed,
installation_date, maintenance_date, user_id)
values
('Big One', 'The big one turning slow', 'Island', 25, '2000-01-01', '2014-01-01', (select id from users where username = 'Marvin'));