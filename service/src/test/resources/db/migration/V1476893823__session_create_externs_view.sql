/***********************************************************************************************************************
  File: V1476893823__session_create_externs_view.sql

  Description: the view externs is used

***********************************************************************************************************************/

USE session;
CREATE OR REPLACE VIEW externs AS
  SELECT
    DISTINCT x.clientname AS clientname,
             e.testeedb AS testeedb,
             e.proctordb AS proctordb,
             'session' AS sessiondb,
             e.testeetype AS testeetype,
             e.proctortype AS proctortype,
             e.clientstylepath AS clientstylepath,
             x.environment AS environment,
             e.ispracticetest AS ispracticetest,
             e.timezoneoffset AS timezoneoffset,
             e.publishurl AS publishurl,
             e.initialreportingid AS initialreportingid,
             e.initialsessionid AS initialsessionid,
             e.testdb AS testdb,
             e.qabrokerguid AS qabrokerguid
  FROM
    session._externs x
    JOIN
    configs.client_externs e
      ON (e.clientname = x.clientname
          AND e.environment = x.environment);