package tds.session.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ExternalSessionConfigurationRepositoryImpl(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<ExternalSessionConfiguration> findExternalSessionConfigurationByClientName(String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        String query = "SELECT " +
            "externs.clientname as clientName, \n" +
            "externs.environment, \n" +
            "_externs.shiftwindowstart, \n" +
            "_externs.shiftwindowend, \n " +
            "_externs.shiftformstart, \n " +
            "_externs.shiftformend \n " +
            "FROM session.externs \n" +
            "JOIN session._externs ON _externs.clientName = externs.clientName \n" +
            "WHERE externs.clientname = :clientName";

        Optional<ExternalSessionConfiguration> maybeExtern;
        try {
            maybeExtern = Optional.of(jdbcTemplate.queryForObject(query, parameters, (rs, rowNum) ->
                new ExternalSessionConfiguration(
                    rs.getString("clientName"),
                    rs.getString("environment"),
                    rs.getInt("shiftwindowstart"),
                    rs.getInt("shiftwindowend"),
                    rs.getInt("shiftformstart"),
                    rs.getInt("shiftformend")
                )
            ));
        } catch (EmptyResultDataAccessException e) {
            maybeExtern = Optional.empty();
        }

        return maybeExtern;
    }
}
