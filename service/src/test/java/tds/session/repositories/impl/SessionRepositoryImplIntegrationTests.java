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

        insertSession(session);

        Optional<Session> sessionOptional = sessionRepository.findSessionById(sessionId);
        assertThat(sessionOptional).isPresent();
        assertThat(sessionOptional.get().getId()).isEqualTo(sessionId);
        assertThat(sessionOptional.get().getStatus()).isEqualTo("closed");
        assertThat(sessionOptional.get().getDateBegin()).isEqualByComparingTo(dateBegin);
        assertThat(sessionOptional.get().getDateChanged()).isEqualTo(dateChanged);
        assertThat(sessionOptional.get().getDateEnd()).isEqualTo(dateEnded);
        assertThat(sessionOptional.get().getDateVisited()).isEqualTo(dateVisited);
        assertThat(sessionOptional.get().getClientName()).isEqualTo("SBAC_PT");
        assertThat(sessionOptional.get().getProctorId()).isEqualTo(23);
        assertThat(sessionOptional.get().getBrowserKey()).isEqualTo(browserKey);
        assertThat(sessionOptional.get().getProctorName()).isEqualTo(proctorName);
        assertThat(sessionOptional.get().getProctorEmail()).isEqualTo(proctorEmail);
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

        insertSession(session);

        Optional<Session> sessionOptional = sessionRepository.findSessionById(sessionId);
        assertThat(sessionOptional).isPresent();
        assertThat(sessionOptional.get().getId()).isEqualTo(sessionId);
        assertThat(sessionOptional.get().getStatus()).isEqualTo("open");
        assertThat(sessionOptional.get().getDateBegin()).isNull();
        assertThat(sessionOptional.get().getDateEnd()).isNull();
        assertThat(sessionOptional.get().getDateChanged()).isNull();
        assertThat(sessionOptional.get().getDateVisited()).isNull();
        assertThat(sessionOptional.get().getProctorId()).isEqualTo(99L);
        assertThat(sessionOptional.get().getBrowserKey()).isEqualTo(browserKey);
    }

    @Test
    public void shouldHandleWhenSessionCannotBeFoundById() {
        Optional<Session> sessionOptional = sessionRepository.findSessionById(UUID.randomUUID());
        assertThat(sessionOptional).isNotPresent();
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

        insertSession(session);

        sessionRepository.pause(sessionId);

        Optional<Session> result = sessionRepository.findSessionById(sessionId);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(sessionId);
        assertThat(result.get().getStatus()).isEqualTo("closed");
        assertThat(result.get().getDateChanged()).isNotNull();
        assertThat(result.get().getDateChanged()).isGreaterThan(result.get().getDateBegin());
        assertThat(result.get().getDateEnd()).isNotNull();
        assertThat(result.get().getDateEnd()).isGreaterThan(result.get().getDateBegin());
    }

    @Test
    public void shouldUpdateSessionDateVisited() {
        Session session = new Session.Builder()
            .fromSession(random(Session.class))
            .withDateVisited(Instant.now().minus(99999))
            .build();
        insertSession(session);

        assertThat(session.getDateVisited()).isNotNull();

        Optional<Session> retSession = sessionRepository.findSessionById(session.getId());
        assertThat(retSession).isPresent();

        Instant priorDateVisited = retSession.get().getDateVisited();

        sessionRepository.updateDateVisited(session.getId());
        Optional<Session> updatedSession = sessionRepository.findSessionById(session.getId());
        assertThat(updatedSession).isPresent();
        assertThat(priorDateVisited.isBefore(updatedSession.get().getDateVisited())).isTrue();
    }

    private void insertSession(Session session) {
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

        parameters = new MapSqlParameterSource("userId", UUID.randomUUID().toString())
            .addValue("userKey", session.getProctorId())
            .addValue("email", session.getProctorEmail());

        jdbcTemplate.update(tblUserInsertSQL, parameters);
    }
}
