/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.session.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import tds.common.data.mysql.UuidAdapter;
import tds.session.Session;
import tds.session.repositories.SessionRepository;

import static tds.common.data.mapping.ResultSetMapperUtility.mapInstantToTimestamp;
import static tds.common.data.mapping.ResultSetMapperUtility.mapTimestampToJodaInstant;

@Repository
class SessionRepositoryImpl implements SessionRepository {
    private static final Logger log = LoggerFactory.getLogger(SessionRepositoryImpl.class);
    private static final RowMapper<Session> sessionRowMapper = new SessionRowMapper();
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public SessionRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Session> findSessionsByIds(final UUID... ids) {
        final SqlParameterSource parameters = new MapSqlParameterSource("ids",
            Arrays.asList(ids).stream()
                .map(id -> UuidAdapter.getBytesFromUUID(id))
                .collect(Collectors.toList())
        );

        String query =
            "SELECT \n" +
                "   s._key AS id, \n" +
                "   s.sessionid AS sessionId, \n" +
                "   s.status, \n" +
                "   s.datebegin, \n" +
                "   s.dateend, \n" +
                "   s.datechanged, \n" +
                "   s.datevisited, \n" +
                "   s.clientname, \n" +
                "   s._efk_proctor AS proctorId, \n" +
                "   s._fk_browser AS browserKey, \n" +
                "   s.proctorName, \n" +
                "   u.email as proctorEmail \n" +
                "FROM \n" +
                "   session.session s\n" +
                "LEFT JOIN \n " +
                "   session.tbluser u \n" +
                "ON s._efk_proctor = u.userkey \n" +
                "WHERE \n" +
                "   s._key IN (:ids)";

        return jdbcTemplate.query(query, parameters, sessionRowMapper);
    }

    @Override
    public void pause(final UUID sessionId) {
        // Had to build the UTC Timestamp this way.  Using Timestamp utcTs = Timestamp.from(Instant.now()) would always
        // result in a Timestamp that reflected my local system clock settings.  Want to guarantee that dates/times are
        // always UTC, regardless of system clock.
        Timestamp utcTs = Timestamp.valueOf(LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")));

        final SqlParameterSource parameters =
            new MapSqlParameterSource("id", UuidAdapter.getBytesFromUUID(sessionId))
                .addValue("dateChanged", utcTs)
                .addValue("dateEnd", utcTs);

        // In order to preserve compatibility with the existing TDS system, this implementation executes an UPDATE
        // against the existing record in the session.session table.
        final String SQL =
            "UPDATE \n" +
                "   session.session \n" +
                "SET \n" +
                "   status = 'closed', \n" +
                "   datechanged = :dateChanged, \n" +
                "   dateend = :dateEnd \n" +
                "WHERE \n" +
                "   _key = :id";

        try {
            jdbcTemplate.update(SQL, parameters);
        } catch (DataAccessException e) {
            log.error("{} UPDATE threw exception", SQL, e);
            throw e;
        }
    }

    @Override
    public void updateDateVisited(final UUID sessionId) {
        final SqlParameterSource parameters =
            new MapSqlParameterSource("id", UuidAdapter.getBytesFromUUID(sessionId))
                .addValue("dateVisited", mapInstantToTimestamp(Instant.now()));

        // In order to preserve compatibility with the existing TDS system, this implementation executes an UPDATE
        // against the existing record in the session.session table.
        final String SQL =
            "UPDATE \n" +
                "   session.session \n" +
                "SET \n" +
                "   datevisited = :dateVisited \n" +
                "WHERE \n" +
                "   _key = :id";

        try {
            jdbcTemplate.update(SQL, parameters);
        } catch (DataAccessException e) {
            log.error("{} UPDATE threw exception", SQL, e);
            throw e;
        }
    }

    private static class SessionRowMapper implements RowMapper<Session> {

        @Override
        public Session mapRow(ResultSet rs, int i) throws SQLException {
            return new Session.Builder()
                .withId(UuidAdapter.getUUIDFromBytes(rs.getBytes("id")))
                .withSessionKey(rs.getString("sessionId"))
                .withStatus(rs.getString("status"))
                .withDateBegin(mapTimestampToJodaInstant(rs, "datebegin"))
                .withDateEnd(mapTimestampToJodaInstant(rs, "dateend"))
                .withDateChanged(mapTimestampToJodaInstant(rs, "datechanged"))
                .withDateVisited(mapTimestampToJodaInstant(rs, "datevisited"))
                .withClientName(rs.getString("clientname"))
                .withProctorId((Long) rs.getObject("proctorId")) // proctorId can be null in the db table.
                .withProctorName(rs.getString("proctorName"))
                .withBrowserKey(UuidAdapter.getUUIDFromBytes(rs.getBytes("browserKey")))
                .withProctorEmail(rs.getString("proctorEmail"))
                .build();
        }
    }
}