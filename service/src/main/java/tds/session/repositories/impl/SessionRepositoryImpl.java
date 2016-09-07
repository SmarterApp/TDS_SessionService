package tds.session.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

import tds.common.data.mysql.UuidAdapter;
import tds.common.data.mysql.spring.UuidBeanPropertyRowMapper;
import tds.session.Session;
import tds.session.repositories.SessionRepository;

@Repository
class SessionRepositoryImpl implements SessionRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public SessionRepositoryImpl(DataSource datasource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(datasource);
    }

    @Override
    public Optional<Session> getSessionById(UUID id) {
        final SqlParameterSource parameters = new MapSqlParameterSource("id", UuidAdapter.getBytesFromUUID(id));

        String query = "SELECT _Key as id, sessionType as type, status \n" +
                       "FROM session.session \n" +
                       "WHERE _Key = :id";

        Optional<Session> sessionOptional;
        try {
            sessionOptional = Optional.of(jdbcTemplate.queryForObject(query, parameters, new UuidBeanPropertyRowMapper<>(Session.class)));
        } catch (IncorrectResultSizeDataAccessException e) {
            sessionOptional = Optional.empty();
        }


        return sessionOptional;
    }
}
