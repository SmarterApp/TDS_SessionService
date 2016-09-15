package tds.session.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import tds.common.data.mysql.UuidAdapter;
import tds.common.data.mysql.spring.UuidBeanPropertyRowMapper;
import tds.session.Session;
import tds.session.repositories.SessionRepository;

@Repository
class SessionRepositoryImpl implements SessionRepository {
    private static final Logger log = LoggerFactory.getLogger(SessionRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public SessionRepositoryImpl(DataSource datasource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(datasource);
    }

    @Override
    public Optional<Session> getSessionById(UUID id) {
        final SqlParameterSource parameters = new MapSqlParameterSource("id", UuidAdapter.getBytesFromUUID(id));

        String query =
                "SELECT \n" +
                "   s._key AS id, \n" +
                "   s.sessiontype AS `type`, \n" +
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
            sessionOptional = Optional.of(jdbcTemplate.queryForObject(query, parameters, new UuidBeanPropertyRowMapper<>(Session.class)));
        } catch (EmptyResultDataAccessException e) {
            sessionOptional = Optional.empty();
        }

        return sessionOptional;
    }

    /**
     * In order to preserve compatibility with the existing TDS system, this implementation executes an UPDATE against
     * the existing record in the {@code session.session} table.  Once the new code has complete responsibility for
     * managing a {@link Session}, this implementation will be updated to comply with the "immutability" database design
     * concept.
     *
     * @param sessionId The id of the {@link Session} to pause
     * @param newStatus The status of the {@link Session} is being paused
     */
    @Override
    public void pause(final UUID sessionId, final String newStatus) {
        Timestamp utcNow = Timestamp.from(Instant.now());
        final SqlParameterSource parameters =
                new MapSqlParameterSource("id", UuidAdapter.getBytesFromUUID(sessionId))
                        .addValue("reason", newStatus)
                        .addValue("dateChanged", utcNow)
                        .addValue("dateEnd", utcNow);

        final String SQL =
                "UPDATE \n" +
                "   session.session \n" +
                "SET \n" +
                "   status = :reason, \n" +
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
}
