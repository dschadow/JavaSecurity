CREATE TABLE `ADVERTISE` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ferriswheel_id` int(11) NOT NULL,
  `advertise_date` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `ferris_wheel_idx` (`ferriswheel_id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `ferris_wheel_fk` FOREIGN KEY (`ferriswheel_id`) REFERENCES `FERRIS_WHEELS` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=ucs2;
