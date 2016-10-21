package tds.session.repositories.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

import tds.session.SessionAssessment;
import tds.session.repositories.SessionAssessmentQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static tds.common.data.mysql.UuidAdapter.getBytesFromUUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class SessionAssessmentQueryRepositoryImplIntegrationTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private SessionAssessmentQueryRepository repository;

    private UUID sessionUUID;
    private static final String ASSESSMENT_ID = "SBAC-ELA-3";
    private static final String ASSESSMENT_KEY = "(SBAC_PT)SBAC-ELA-3-Spring-2013-2015";

    @Before
    public void setUp() {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        sessionUUID = UUID.randomUUID();
        String sessionInsertSQL = "INSERT INTO session " +
            "VALUES (:sessionId ,5,'57325f70e4b0ed2c55c37e3d','CA Admin','Adm-99','closed','',NULL,'2016-08-18 18:25:07.161','2016-08-18 18:25:07.115'," +
            "'2016-08-18 18:26:31.669','ip-172-31-33-51',NULL,'2016-08-18 18:26:31.669','2016-08-18 18:25:07.115','SBAC_PT'," +
            ":browserKeyId,'Development',0,NULL,2,0,NULL,NULL,NULL);";

        SqlParameterSource parameters = new MapSqlParameterSource("sessionId", getBytesFromUUID(sessionUUID))
            .addValue("browserKeyId", getBytesFromUUID(UUID.randomUUID()));
        jdbcTemplate.update(sessionInsertSQL, parameters);

        String sessionTestsInsertSQL = "INSERT INTO sessiontests (_fk_session, _efk_adminsubject, _efk_testid) " +
            "VALUES (:sessionId, :assessmentKey, :assessmentId);";

        parameters = new MapSqlParameterSource("sessionId", getBytesFromUUID(sessionUUID))
            .addValue("assessmentKey", ASSESSMENT_KEY)
            .addValue("assessmentId", ASSESSMENT_ID);
        jdbcTemplate.update(sessionTestsInsertSQL, parameters);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void itShouldReturnEmptyWhenNotFound() {
        List<SessionAssessment> sessionAssessments = repository.findSessionAssessment(UUID.randomUUID());
        assertThat(sessionAssessments).isEmpty();
    }

    @Test
    public void itShouldReturnSessionAssessmentForSession() {
        List<SessionAssessment> sessionAssessments = repository.findSessionAssessment(sessionUUID);
        assertThat(sessionAssessments).isNotEmpty();

        SessionAssessment sessionAssessment = sessionAssessments.get(0);
        assertThat(sessionAssessment.getAssessmentId()).isEqualTo(ASSESSMENT_ID);
        assertThat(sessionAssessment.getAssessmentKey()).isEqualTo(ASSESSMENT_KEY);
        assertThat(sessionAssessment.getSessionId()).isEqualTo(sessionUUID);
    }
}
