/***********************************************************************************************************************
  File: V1476890753__session_drop_externs_table.sql

  Desc: externs is actually a view not a table.  this should be dropped.

***********************************************************************************************************************/
use session;

drop table if exists externs;