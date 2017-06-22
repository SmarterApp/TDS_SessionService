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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import tds.session.ExternalSessionConfiguration;
import tds.session.repositories.ExternalSessionConfigurationRepository;

@Repository
public class ExternalSessionConfigurationRepositoryImpl implements ExternalSessionConfigurationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ExternalSessionConfigurationRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ExternalSessionConfiguration> findExternalSessionConfigurationByClientName(final String clientName) {
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
