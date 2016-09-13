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
import java.time.Instant;
import java.util.Date;
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

    @Override
    public void pause(final Session session, final String reason) {
        Instant now = Instant.now();
        final SqlParameterSource parameters =
                new MapSqlParameterSource("id", UuidAdapter.getBytesFromUUID(session.getId()))
                        .addValue("reason", reason)
                        .addValue("dateChanged", now)
                        .addValue("dateEnd", now);

        final String SQL =
                "UPDATE\n" +
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
            log.error(String.format("%s UPDATE threw exception", SQL), e);
            throw e;
        }
    }
}
