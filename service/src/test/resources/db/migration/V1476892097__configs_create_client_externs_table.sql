/***********************************************************************************************************************
  File: V1476892097__create_configs_client_externs.sql

  Description: The view externs needs to use the client_

***********************************************************************************************************************/
USE configs;
DROP TABLE IF EXISTS client_externs;
CREATE TABLE client_externs (
  _key varbinary(16) NOT NULL,
  testeedb varchar(255) NOT NULL,
  proctordb varchar(255) NOT NULL,
  testdb varchar(255) NOT NULL,
  scorerdb varchar(255) DEFAULT NULL,
  archivedb varchar(255) DEFAULT NULL,
  testeetype varchar(255) NOT NULL DEFAULT 'rts',
  proctortype varchar(255) NOT NULL DEFAULT 'rts',
  scorertype varchar(255) NOT NULL DEFAULT 'rts',
  sessiondb varchar(255) DEFAULT NULL,
  errorsserver varchar(255) DEFAULT NULL,
  errorsdb varchar(255) DEFAULT NULL,
  clientname varchar(100) NOT NULL,
  bankadminkey varchar(150) DEFAULT NULL,
  clientstylepath varchar(500) DEFAULT NULL,
  qaxmlpath varchar(500) DEFAULT NULL,
  environment varchar(100) NOT NULL,
  ispracticetest bit(1) NOT NULL,
  timezoneoffset int(11) NOT NULL DEFAULT 0,
  publishurl varchar(255) DEFAULT NULL,
  initialreportingid bigint(20) NOT NULL DEFAULT 1,
  initialsessionid bigint(20) NOT NULL DEFAULT 1,
  qabrokerguid varbinary(16) DEFAULT NULL,
  expiresatminutes int(11) DEFAULT NULL,
  PRIMARY KEY (_key),
  UNIQUE KEY ix_clientexterns (clientname,environment)
);

INSERT INTO client_externs VALUES (X'1FEFBFBD54EFBFBDEFBFBD154BEFBFBD','MultiClient_RTS_2013','MultiClient_RTS_2013','itembank',1,1,'RTS','RTS',1,'session',1,1,'SBAC',1,'SBAC',NULL,'Development',0,0,NULL,100000,1,NULL,NULL);
INSERT INTO client_externs VALUES (X'64EFBFBDEFBFBDEFBFBD68464D42EFBF','MultiClient_RTS_2013','MultiClient_RTS_2013','itembank',1,1,'RTS','RTS',1,'session',1,1,'SBAC_PT',1,'SBAC',NULL,'Development',1,0,NULL,100000,1,NULL,NULL);
