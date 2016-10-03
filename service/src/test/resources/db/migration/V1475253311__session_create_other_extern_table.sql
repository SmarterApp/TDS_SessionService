/***********************************************************************************************************************
  File: V1475253311__session_create_other_extern_table.sql

  Desc: Creates an _extern table.  This table stores some specific session extern data.  Existing table in the database

***********************************************************************************************************************/
USE session;

DROP TABLE IF EXISTS _externs;

CREATE TABLE _externs (
  clientname varchar(100) NOT NULL,
  shiftwindowstart int(11) NOT NULL DEFAULT 0,
  shiftwindowend int(11) NOT NULL DEFAULT 0
);

INSERT INTO _externs VALUES ('SBAC', 5, 3);
INSERT INTO _externs VALUES ('SBAC-PT', 0, 0);