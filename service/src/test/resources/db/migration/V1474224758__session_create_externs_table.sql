/***********************************************************************************************************************
  File: V1474224758__session_create_externs_table.sql

  Desc: Creates an extern table.  This is a view in the legacy student system but for purposes of testing it is simpler
  to just add a table.

***********************************************************************************************************************/
USE session;
DROP TABLE IF EXISTS externs;

CREATE TABLE externs (
  clientname varchar(100) NOT NULL,
  environment varchar(100) NOT NULL
);

INSERT INTO externs VALUES ('SBAC', 'SIMULATION');
INSERT INTO externs VALUES ('SBAC-PT', 'Development');