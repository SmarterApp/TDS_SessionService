package tds.session.repositories.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

import tds.session.Extern;
import tds.session.repositories.ExternRepository;

@Repository
public class ExternRepositoryImpl implements ExternRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ExternRepositoryImpl(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<Extern> getExternByClientName(String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        String query = "SELECT " +
            "clientname as clientName, \n" +
            "environment \n" +
            "FROM session.externs \n" +
            "WHERE clientname = :clientName";

        Optional<Extern> externOptional;
        try {
            externOptional = Optional.of(jdbcTemplate.queryForObject(query, parameters, (rs, rowNum) -> new Extern.Builder()
                .withClientName(rs.getString("clientName"))
                .withEnvironment(rs.getString("environment"))
                .build()));
        } catch (EmptyResultDataAccessException e) {
            externOptional = Optional.empty();
        }

        return externOptional;
    }
}
