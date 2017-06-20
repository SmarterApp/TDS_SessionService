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

import org.joda.time.Instant;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tds.session.Session;
import tds.session.repositories.SessionRepository;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static tds.common.data.mapping.ResultSetMapperUtility.mapJodaInstantToTimestamp;
import static tds.common.data.mysql.UuidAdapter.getBytesFromUUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class SessionRepositoryImplIntegrationTests {
    private final static String sessionInsertSQL = "INSERT INTO session (\n" +
        "  _key,\n" +
        "  datecreated,\n" +
        "  clientname,\n" +
        "  _fk_browser,\n" +
        "  environment,\n" +
        "  status,\n" +
        "  _efk_proctor,\n" +
        "  datechanged,\n" +
        "  dateend,\n" +
        "  datebegin, \n" +
        "  datevisited, \n" +
        "  proctorName \n" +
        ") VALUES (\n" +
        "  :key,\n" +
        "  :dateCreated,\n" +
        "  :clientName,\n" +
        "  :browserId,\n" +
        "  :environment,\n" +
        "  :status,\n" +
        "  :proctorId,\n" +
        "  :dateChanged,\n" +
        "  :dateEnd,\n" +
        "  :dateBegin,\n" +
        "  :dateVisited, \n" +
        "  :proctorName \n" +
        ");";

    private final static String tblUserInsertSQL =
        "INSERT INTO tbluser ( \n" +
            "   userid, \n" +
            "   userkey, \n" +
            "   email \n" +
            ") \n" +
            "VALUES ( \n" +
            "   :userId, \n" +
            "   :userKey, \n" +
            "   :email \n" +
            ");";

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @After
    public void tearDown() {
    }

    @Test
    public void shouldRetrieveSessionsForIds() throws ParseException {
        Session session1 = random(Session.class);
        Session session2 = random(Session.class);

        insertSession(session1, true);
        insertSession(session2, true);

        List<Session> retSessions = sessionRepository.findSessionsByIds(session1.getId(), session2.getId());

        Session retSession1 = null;
        Session retSession2 = null;

        for (Session s : retSessions) {
            if (s.getId().equals(session1.getId())) {
                retSession1 = s;
            } else if (s.getId().equals(session2.getId())) {
                retSession2 = s;
            }
        }

        assertThat(retSession1.getId()).isEqualTo(session1.getId());
        assertThat(retSession1.getStatus()).isEqualTo(session1.getStatus());
        assertThat(retSession1.getDateBegin()).isEqualByComparingTo(session1.getDateBegin());
        assertThat(retSession1.getDateChanged()).isEqualTo(session1.getDateChanged());
        assertThat(retSession1.getDateEnd()).isEqualTo(session1.getDateEnd());
        assertThat(retSession1.getDateVisited()).isEqualTo(session1.getDateVisited());
        assertThat(retSession1.getClientName()).isEqualTo(session1.getClientName());
        assertThat(retSession1.getProctorId()).isEqualTo(session1.getProctorId());
        assertThat(retSession1.getBrowserKey()).isEqualTo(session1.getBrowserKey());
        assertThat(retSession1.getProctorName()).isEqualTo(session1.getProctorName());
        assertThat(retSession1.getProctorEmail()).isEqualTo(session1.getProctorEmail());

        assertThat(retSession2).isNotNull();
    }

    @Test
    public void shouldRetrieveSessionForId() throws ParseException {
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();
        final String proctorName = "Proctor";
        final String proctorEmail = "mail@proctor.com";

        Instant dateBegin = Instant.now().minus(10000);
        Instant dateVisited = Instant.now().minus(8000);
        Instant dateChanged = Instant.now().minus(5000);
        Instant dateEnded = Instant.now();

        Session session = new Session.Builder()
            .withId(sessionId)
            .withClientName("SBAC_PT")
            .withBrowserKey(browserKey)
            .withStatus("closed")
            .withProctorId(23L)
            .withDateBegin(dateBegin)
            .withDateVisited(dateVisited)
            .withDateChanged(dateChanged)
            .withProctorName(proctorName)
            .withProctorEmail(proctorEmail)
            .withDateEnd(dateEnded)
            .build();

        insertSession(session, true);

        List<Session> retSessions = sessionRepository.findSessionsByIds(sessionId);
        Session retSession = retSessions.get(0);
        assertThat(retSession.getId()).isEqualTo(sessionId);
        assertThat(retSession.getStatus()).isEqualTo("closed");
        assertThat(retSession.getDateBegin()).isEqualByComparingTo(dateBegin);
        assertThat(retSession.getDateChanged()).isEqualTo(dateChanged);
        assertThat(retSession.getDateEnd()).isEqualTo(dateEnded);
        assertThat(retSession.getDateVisited()).isEqualTo(dateVisited);
        assertThat(retSession.getClientName()).isEqualTo("SBAC_PT");
        assertThat(retSession.getProctorId()).isEqualTo(23);
        assertThat(retSession.getBrowserKey()).isEqualTo(browserKey);
        assertThat(retSession.getProctorName()).isEqualTo(proctorName);
        assertThat(retSession.getProctorEmail()).isEqualTo(proctorEmail);
    }

    @Test
    public void shouldReturnASessionForASessionIdThatHasNullDates() {
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();

        Session session = new Session.Builder()
            .withId(sessionId)
            .withClientName("SBAC_PT")
            .withBrowserKey(browserKey)
            .withStatus("open")
            .withProctorId(99L)
            .build();

        insertSession(session, true);

        List<Session> retSessions = sessionRepository.findSessionsByIds(sessionId);
        Session retSession = retSessions.get(0);
        assertThat(retSession.getId()).isEqualTo(sessionId);
        assertThat(retSession.getStatus()).isEqualTo("open");
        assertThat(retSession.getDateBegin()).isNull();
        assertThat(retSession.getDateEnd()).isNull();
        assertThat(retSession.getDateChanged()).isNull();
        assertThat(retSession.getDateVisited()).isNull();
        assertThat(retSession.getProctorId()).isEqualTo(99L);
        assertThat(retSession.getBrowserKey()).isEqualTo(browserKey);
    }

    @Test
    public void shouldHandleWhenSessionCannotBeFoundById() {
        List<Session> retSessions = sessionRepository.findSessionsByIds(UUID.randomUUID());
        assertThat(retSessions).isEmpty();
    }

    @Test
    public void shouldFindProctorlessSession() {
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();

        Instant dateBegin = Instant.now().minus(10000);

        Session session = new Session.Builder()
            .withId(sessionId)
            .withClientName("SBAC_PT")
            .withBrowserKey(browserKey)
            .withStatus("open")
            .withDateBegin(dateBegin)
            .build();

        insertSession(session, false);

        List<Session> retSessions = sessionRepository.findSessionsByIds(sessionId);
        Session retSession = retSessions.get(0);
        assertThat(retSession.getProctorId()).isNull();
        assertThat(retSession.getProctorEmail()).isNull();
    }

    @Test
    public void shouldPauseASession() {
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();

        Instant dateBegin = Instant.now().minus(10000);

        Session session = new Session.Builder()
            .withId(sessionId)
            .withClientName("SBAC_PT")
            .withBrowserKey(browserKey)
            .withStatus("open")
            .withProctorId(99L)
            .withDateBegin(dateBegin)
            .build();

        insertSession(session, true);

        sessionRepository.pause(sessionId);

        List<Session> results = sessionRepository.findSessionsByIds(sessionId);
        Session retSession = results.get(0);
        assertThat(retSession.getId()).isEqualTo(sessionId);
        assertThat(retSession.getStatus()).isEqualTo("closed");
        assertThat(retSession.getDateChanged()).isNotNull();
        assertThat(retSession.getDateChanged()).isGreaterThan(retSession.getDateBegin());
        assertThat(retSession.getDateEnd()).isNotNull();
        assertThat(retSession.getDateEnd()).isGreaterThan(retSession.getDateBegin());
    }

    @Test
    public void shouldUpdateSessionDateVisited() {
        Session session = new Session.Builder()
            .fromSession(random(Session.class))
            .withDateVisited(Instant.now().minus(99999))
            .build();
        insertSession(session, true);

        assertThat(session.getDateVisited()).isNotNull();

        List<Session> retSessions = sessionRepository.findSessionsByIds(session.getId());
        Session retSession = retSessions.get(0);
        assertThat(retSessions).hasSize(1);

        Instant priorDateVisited = retSession.getDateVisited();

        sessionRepository.updateDateVisited(session.getId());
        List<Session> updateSessions = sessionRepository.findSessionsByIds(session.getId());
        Session updateSession = updateSessions.get(0);
        assertThat(priorDateVisited.isBefore(updateSession.getDateVisited())).isTrue();
    }

    private void insertSession(Session session, boolean insertProctorData) {
        SqlParameterSource parameters = new MapSqlParameterSource("key", getBytesFromUUID(session.getId()))
            .addValue("clientName", session.getClientName())
            .addValue("browserId", getBytesFromUUID(session.getBrowserKey()))
            .addValue("environment", "unitTest")
            .addValue("status", session.getStatus())
            .addValue("proctorId", session.getProctorId())
            .addValue("dateChanged", mapJodaInstantToTimestamp(session.getDateChanged()))
            .addValue("dateVisited", mapJodaInstantToTimestamp(session.getDateVisited()))
            .addValue("dateBegin", mapJodaInstantToTimestamp(session.getDateBegin()))
            .addValue("dateCreated", mapJodaInstantToTimestamp(Instant.now()))
            .addValue("proctorName", session.getProctorName())
            .addValue("dateEnd", mapJodaInstantToTimestamp(session.getDateEnd()));

        jdbcTemplate.update(sessionInsertSQL, parameters);

        if (insertProctorData) {
            parameters = new MapSqlParameterSource("userId", UUID.randomUUID().toString())
                .addValue("userKey", session.getProctorId())
                .addValue("email", session.getProctorEmail());

            jdbcTemplate.update(tblUserInsertSQL, parameters);
        }
    }
}
