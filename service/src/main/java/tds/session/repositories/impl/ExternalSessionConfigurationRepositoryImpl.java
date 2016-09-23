package tds.session.repositories.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

import tds.session.ExternalSessionConfiguration;
import tds.session.repositories.ExternalSessionConfigurationRepository;

@Repository
public class ExternalSessionConfigurationRepositoryImpl implements ExternalSessionConfigurationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ExternalSessionConfigurationRepositoryImpl(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<ExternalSessionConfiguration> findExternalSessionConfigurationByClientName(String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        String query = "SELECT " +
            "clientname as clientName, \n" +
            "environment \n" +
            "FROM session.externs \n" +
            "WHERE clientname = :clientName";

        Optional<ExternalSessionConfiguration> maybeExtern;
        try {
            maybeExtern = Optional.of(jdbcTemplate.queryForObject(query, parameters, (rs, rowNum) ->
                new ExternalSessionConfiguration(rs.getString("clientName"), rs.getString("environment"))));
        } catch (EmptyResultDataAccessException e) {
            maybeExtern = Optional.empty();
        }

        return maybeExtern;
    }
}
