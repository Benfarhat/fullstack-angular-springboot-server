DROP TABLE IF EXISTS `tutorials`;
CREATE TABLE `tutorials` (
  `id` 			int(11) 		PRIMARY KEY AUTO_INCREMENT,
  `title` 		varchar(200) 	NOT NULL,
  `description` text,
  `published` 	tinyint(1) 		DEFAULT NULL
);
COMMIT;