CREATE TABLE `FERRIS_WHEELS` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `location` varchar(50) NOT NULL,
  `speed` int(4) DEFAULT '0',
  `installation_date` date DEFAULT NULL,
  `maintenance_date` date DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `online` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `ferris_wheel_owner` (`user_id`),
  CONSTRAINT `ferris_wheel_owner` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;



insert into ferris_wheels (name, description, location, speed,
installation_date, maintenance_date, user_id, online)
values
('Small One', 'The small one turning fast', 'Near Shore', 125, '2000-01-05', '2014-09-01', (select id from users where username = 'Zaphod'), 1);

insert into ferris_wheels (name, description, location, speed,
installation_date, maintenance_date, user_id, online)
values
('Big One', 'The big one turning slow', 'Off Shore', 25, '2003-11-23', '2015-10-10', (select id from users where username = 'Zaphod'), 1);