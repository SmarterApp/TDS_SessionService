/***********************************************************************************************************************
  File: V1491255661__session_create_table_tbluser.sql

  Description: create the tbluser table - stores proctor metadata

***********************************************************************************************************************/

USE session;

DROP TABLE IF EXISTS tbluser;

CREATE TABLE `tbluser` (
  `userid` varchar(50) NOT NULL,
  `userkey` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(128) DEFAULT NULL,
  `fullname` varchar(75) DEFAULT NULL,
  `clientname` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`userid`),
  KEY `userkey` (`userkey`)
)