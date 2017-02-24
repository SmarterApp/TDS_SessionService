/***********************************************************************************************************************
  File: V1476893725__session_alter__externs_table.sql

  Description: Creating the session tables

***********************************************************************************************************************/

USE session;
DROP TABLE IF EXISTS _externs;

CREATE TABLE _externs (
  clientname varchar(100) NOT NULL,
  environment varchar(50) NOT NULL,
  shiftwindowstart int(11) NOT NULL DEFAULT 0,
  shiftwindowend int(11) NOT NULL DEFAULT 0,
  shiftformstart int(11) NOT NULL DEFAULT 0,
  shiftformend int(11) NOT NULL DEFAULT 0,
  shiftftstart int(11) NOT NULL DEFAULT 0,
  shiftftend int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (clientname)
);

INSERT INTO _externs VALUES ('SBAC','Development',0,0,0,0,0,0);
INSERT INTO _externs VALUES ('SBAC_PT','Development',0,0,0,0,0,0);