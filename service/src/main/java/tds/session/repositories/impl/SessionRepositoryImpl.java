package tds.session.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import tds.session.Session;
import tds.session.repositories.SessionRepository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class SessionRepositoryImpl implements SessionRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public SessionRepositoryImpl(DataSource datasource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(datasource);
    }

    @Override
    public Optional<Session> getSessionById(long id) {
        return null;
    }
}
