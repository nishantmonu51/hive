-- SOURCE 041-HIVE-16556.mysql.sql;
--
-- Table structure for table METASTORE_DB_PROPERTIES
--
CREATE TABLE IF NOT EXISTS `METASTORE_DB_PROPERTIES` (
  `PROPERTY_KEY` varchar(255) NOT NULL,
  `PROPERTY_VALUE` varchar(1000) NOT NULL,
  `DESCRIPTION` varchar(1000),
 PRIMARY KEY(`PROPERTY_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
