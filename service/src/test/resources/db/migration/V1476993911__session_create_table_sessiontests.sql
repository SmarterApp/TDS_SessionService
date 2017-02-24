/***********************************************************************************************************************
  File: V1476993911__session_create_table_sessiontests.sql

  Description: create the sessiontests table

***********************************************************************************************************************/

USE session;

DROP TABLE IF EXISTS sessiontests;

CREATE TABLE sessiontests (
  _fk_session varbinary(16) NOT NULL,
  _efk_adminsubject varchar(255) NOT NULL,
  _efk_testid varchar(200) NOT NULL,
  iterations int(11) DEFAULT NULL,
  opportunities int(11) DEFAULT NULL,
  meanproficiency float DEFAULT NULL,
  sdproficiency float DEFAULT NULL,
  strandcorrelation float DEFAULT NULL,
  sim_threads int(11) DEFAULT '4',
  sim_thinktime int(11) DEFAULT '0',
  handscoreitemtypes varchar(256) DEFAULT NULL,
  PRIMARY KEY (_fk_session,_efk_adminsubject),
  CONSTRAINT fk_sessiontests FOREIGN KEY (_fk_session) REFERENCES session (_key) ON DELETE CASCADE ON UPDATE NO ACTION
);