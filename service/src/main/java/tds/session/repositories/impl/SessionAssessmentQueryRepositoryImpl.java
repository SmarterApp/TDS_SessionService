package tds.session.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
    SessionAssessmentQueryRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<SessionAssessment> findSessionAssessment(UUID sessionId) {
        SqlParameterSource parameters = new MapSqlParameterSource("sessionId", UuidAdapter.getBytesFromUUID(sessionId));

        String SQL = "SELECT _fk_session, _efk_adminsubject, _efk_testid \n" +
            "FROM session.sessiontests \n" +
            "WHERE _fk_session = :sessionId \n" +
            "LIMIT 1;";

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
}
