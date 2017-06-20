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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tds.session.Session;
import tds.session.SessionAssessment;
import tds.session.repositories.SessionAssessmentQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static tds.common.data.mysql.UuidAdapter.getBytesFromUUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class SessionAssessmentQueryRepositoryImplIntegrationTests {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private SessionAssessmentQueryRepository repository;

    private UUID sessionUUID;
    private static final String ASSESSMENT_ID1 = "SBAC-ELA-3";
    private static final String ASSESSMENT_KEY1 = "(SBAC_PT)SBAC-ELA-3-Spring-2013-2015";
    private static final String ASSESSMENT_ID2 = "SBAC-ELA-5";
    private static final String ASSESSMENT_KEY2 = "(SBAC_PT)SBAC-ELA-5-Spring-2013-2015";

    @Before
    public void setUp() {
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
            .addValue("assessmentKey", ASSESSMENT_KEY1)
            .addValue("assessmentId", ASSESSMENT_ID1);
        jdbcTemplate.update(sessionTestsInsertSQL, parameters);

        parameters = new MapSqlParameterSource("sessionId", getBytesFromUUID(sessionUUID))
            .addValue("assessmentKey", ASSESSMENT_KEY2)
            .addValue("assessmentId", ASSESSMENT_ID2);
        jdbcTemplate.update(sessionTestsInsertSQL, parameters);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void itShouldReturnEmptyWhenNotFound() {
        Optional<SessionAssessment> maybeSessionAssessment = repository.findSessionAssessment(UUID.randomUUID(), "");
        assertThat(maybeSessionAssessment).isNotPresent();
    }

    @Test
    public void itShouldReturnSessionAssessmentForSession() {
        Optional<SessionAssessment> maybeSessionAssessment = repository.findSessionAssessment(sessionUUID, ASSESSMENT_KEY1);
        assertThat(maybeSessionAssessment).isPresent();

        SessionAssessment sessionAssessment = maybeSessionAssessment.get();
        assertThat(sessionAssessment.getAssessmentId()).isEqualTo(ASSESSMENT_ID1);
        assertThat(sessionAssessment.getAssessmentKey()).isEqualTo(ASSESSMENT_KEY1);
        assertThat(sessionAssessment.getSessionId()).isEqualTo(sessionUUID);
    }

    @Test
    public void shouldReturnAllSessionAssessmentsForSession() {
        List<SessionAssessment> sessionAssessments = repository.findSessionAssessments(sessionUUID);
        assertThat(sessionAssessments).hasSize(2);

        SessionAssessment sessionAssessment1 = null;
        SessionAssessment sessionAssessment2 = null;

        for (SessionAssessment sa : sessionAssessments) {
            if (sa.getAssessmentKey().equals(ASSESSMENT_KEY1)) {
                sessionAssessment1 = sa;
            } else if (sa.getAssessmentKey().equals(ASSESSMENT_KEY2)) {
                sessionAssessment2 = sa;
            }
        }

        assertThat(sessionAssessment1.getAssessmentId()).isEqualTo(ASSESSMENT_ID1);
        assertThat(sessionAssessment1.getSessionId()).isEqualTo(sessionUUID);
        assertThat(sessionAssessment2.getAssessmentId()).isEqualTo(ASSESSMENT_ID2);
        assertThat(sessionAssessment2.getSessionId()).isEqualTo(sessionUUID);
    }
}
