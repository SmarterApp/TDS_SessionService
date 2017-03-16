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
import java.util.Optional;
import java.util.UUID;

import tds.common.data.mysql.UuidAdapter;
import tds.session.Session;
import tds.session.repositories.SessionRepository;

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
    public Optional<Session> findSessionById(final UUID id) {
        final SqlParameterSource parameters = new MapSqlParameterSource("id", UuidAdapter.getBytesFromUUID(id));

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
                "   s._fk_browser AS browserKey \n" +
                "FROM \n" +
                "   session.session s\n" +
                "WHERE \n" +
                "   s._key = :id";

        Optional<Session> sessionOptional;
        try {
            sessionOptional = Optional.of(jdbcTemplate.queryForObject(query, parameters, sessionRowMapper));
        } catch (EmptyResultDataAccessException e) {
            sessionOptional = Optional.empty();
        }

        return sessionOptional;
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
                .withBrowserKey(UuidAdapter.getUUIDFromBytes(rs.getBytes("browserKey")))
                .build();
        }
    }
}