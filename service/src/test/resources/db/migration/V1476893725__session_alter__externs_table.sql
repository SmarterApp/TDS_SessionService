/***********************************************************************************************************************
  File: V1476893725__session_alter__externs_table.sql

  Description: Adding the environment column

***********************************************************************************************************************/

use session;

ALTER TABLE _externs ADD COLUMN environment varchar(50);

UPDATE _externs SET environment = 'Development';

ALTER TABLE _externs MODIFY COLUMN environment varchar(50) NOT NULL;