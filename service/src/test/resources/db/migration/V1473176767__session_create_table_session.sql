/***********************************************************************************************************************
  File: V1473176767__session_create_table_session.sql

  Desc: Create the session table the session service relies upon and populate it with a single record.

***********************************************************************************************************************/
USE session;
DROP TABLE IF EXISTS session;

CREATE TABLE session (
  _key varbinary(16) NOT NULL,
  _efk_proctor bigint(20) DEFAULT NULL,
  proctorid varchar(128) DEFAULT NULL,
  proctorname varchar(128) DEFAULT NULL,
  sessionid varchar(128) DEFAULT NULL,
  status varchar(32) NOT NULL DEFAULT 'closed',
  name varchar(255) NOT NULL DEFAULT 'new session',
  description varchar(1024) DEFAULT NULL,
  datecreated datetime(3) NOT NULL,
  datebegin datetime(3) DEFAULT NULL,
  dateend datetime(3) DEFAULT NULL,
  serveraddress varchar(255) DEFAULT NULL,
  reserved varchar(255) DEFAULT NULL,
  datechanged datetime(3) DEFAULT NULL,
  datevisited datetime(3) DEFAULT NULL,
  clientname varchar(100) NOT NULL,
  _fk_browser varbinary(16) NOT NULL,
  environment varchar(50) NOT NULL,
  sessiontype int(11) NOT NULL DEFAULT 0,
  sim_language varchar(50) DEFAULT NULL,
  sim_proctordelay int(11) NOT NULL DEFAULT 2,
  sim_abort bit(1) NOT NULL DEFAULT 0,
  sim_status varchar(25) DEFAULT NULL,
  sim_start datetime(3) DEFAULT NULL,
  sim_stop datetime(3) DEFAULT NULL,
  PRIMARY KEY (_key),
  UNIQUE KEY ix_sessionid (sessionid,clientname),
  KEY ix_sessionproctor (_efk_proctor,clientname)
);

INSERT INTO session VALUES (X'06485031B2B64CEDA0C1B294EDA54DB2',5,'57325f70e4b0ed2c55c37e3d','CA Admin','Adm-44','closed','',NULL,'2016-08-18 18:25:07.161','2016-08-18 18:25:07.115','2016-08-18 18:26:31.669','ip-172-31-33-51',NULL,'2016-08-18 18:26:31.669','2016-08-18 18:25:07.115','SBAC_PT',X'CB5C658D4B32463D9DFA119052E27474','Development',0,NULL,2,0,NULL,NULL,NULL);
INSERT INTO session VALUES (X'08A57E3F3A8744C582A65B473E60785E',6,'57b5d67de4b00c5888e58842','msb admin','adm-52','open','', NULL,'2016-08-29 14:23:50.966','2016-08-29 14:23:50.925','2020-08-29 22:23:50.925','ip-172-31-33-51',NULL,NULL,'2020-08-29 14:28:32.963','SBAC_PT',X'F7A0375CC63A4164976EE883C2D13F62','Development',0,NULL,2,0,NULL,NULL,NULL);
INSERT INTO session VALUES (X'C7F1D37F8A4A4AD9BF7A915068C9D40D',6,'57b5d67de4b00c5888e58842','msb admin','adm-53','open','', NULL,'2016-08-29 14:23:50.966','2016-08-29 14:23:50.925','2020-08-29 22:23:50.925','ip-172-31-33-51',NULL,NULL,'2020-08-29 14:28:32.963','SBAC_PT',X'F7A0375CC63A4164976EE883C2D13F62','Development',0,NULL,2,0,NULL,NULL,NULL);
INSERT INTO session VALUES (X'A976E970F80C4107830EB1020053DE96',6,'57b5d67de4b00c5888e58842','msb admin','adm-54','open','', NULL,'2016-08-29 14:23:50.966',NULL,NULL,'ip-172-31-33-51',NULL,NULL,NULL,'SBAC_PT',X'F7A0375CC63A4164976EE883C2D13F62','Development',0,NULL,2,0,NULL,NULL,NULL);


