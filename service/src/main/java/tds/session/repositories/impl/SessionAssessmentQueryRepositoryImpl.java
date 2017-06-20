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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tds.common.data.mysql.UuidAdapter;
import tds.session.SessionAssessment;
import tds.session.repositories.SessionAssessmentQueryRepository;

@Repository
class SessionAssessmentQueryRepositoryImpl implements SessionAssessmentQueryRepository {
    private static final Logger LOG = LoggerFactory.getLogger(SessionAssessmentQueryRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    SessionAssessmentQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<SessionAssessment> findSessionAssessment(final UUID sessionId, final String assessmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("assessmentKey", assessmentKey)
            .addValue("sessionId", UuidAdapter.getBytesFromUUID(sessionId));

        String SQL = "SELECT _fk_session, _efk_adminsubject, _efk_testid \n" +
            "FROM session.sessiontests \n" +
            "WHERE _fk_session = :sessionId AND _efk_adminsubject = :assessmentKey;";

        Optional<SessionAssessment> maybeSessionAssessment = Optional.empty();
        try {
            maybeSessionAssessment = Optional.of(jdbcTemplate.queryForObject(SQL, parameters, (rs, rowNum) ->
                new SessionAssessment(
                    UuidAdapter.getUUIDFromBytes(rs.getBytes("_fk_session")),
                    rs.getString("_efk_testid"),
                    rs.getString("_efk_adminsubject")
                )
            ));
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("Did not find session assessment for %s", sessionId);
        }

        return maybeSessionAssessment;
    }

    @Override
    public List<SessionAssessment> findSessionAssessments(final UUID sessionId) {
        SqlParameterSource parameters = new MapSqlParameterSource("sessionId", UuidAdapter.getBytesFromUUID(sessionId));

        String SQL = "SELECT _fk_session, _efk_adminsubject, _efk_testid \n" +
            "FROM session.sessiontests \n" +
            "WHERE _fk_session = :sessionId";

        return jdbcTemplate.query(SQL, parameters, (rs, rowNum) ->
            new SessionAssessment(
                UuidAdapter.getUUIDFromBytes(rs.getBytes("_fk_session")),
                rs.getString("_efk_testid"),
                rs.getString("_efk_adminsubject")
            )
        );
    }
}
