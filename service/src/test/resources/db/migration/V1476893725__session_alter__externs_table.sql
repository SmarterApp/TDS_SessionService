/***********************************************************************************************************************
  File: V1476893725__session_alter__externs_table.sql

  Description: Adding the environment column

***********************************************************************************************************************/

use session;

DROP TABLE IF EXISTS _externs;

CREATE TABLE _externs (
  clientname varchar(100) NOT NULL,
  shiftwindowstart int(11) NOT NULL DEFAULT 0,
  shiftwindowend int(11) NOT NULL DEFAULT 0,
  environment varchar(50) NOT NULL
);

INSERT INTO _externs VALUES ('SBAC', 5, 3, 'Development');
INSERT INTO _externs VALUES ('SBAC-PT', 0, 0, 'Development');