CREATE TABLE `ADVERTISE` (
  `id` int(11) NOT NULL,
  `ferris_wheel_id` int(11) NOT NULL,
  `advertise_date` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `ferris_wheel_idx` (`ferris_wheel_id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ferris_wheel_fk` FOREIGN KEY (`ferris_wheel_id`) REFERENCES `FERRIS_WHEELS` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=ucs2;
