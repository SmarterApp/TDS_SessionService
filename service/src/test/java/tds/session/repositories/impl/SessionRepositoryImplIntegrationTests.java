package tds.session.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import tds.session.Session;
import tds.session.SessionServiceApplication;
import tds.session.repositories.SessionRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SessionServiceApplication.class)
public class SessionRepositoryImplIntegrationTests {

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    public void shouldRetrieveSessionForId() throws ParseException {
        UUID sessionId = UUID.fromString("06485031-B2B6-4CED-A0C1-B294EDA54DB2");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Optional<Session> sessionOptional = sessionRepository.getSessionById(sessionId);
        assertThat(sessionOptional).isPresent();
        assertThat(sessionOptional.get().getId()).isEqualTo(sessionId);
        assertThat(sessionOptional.get().getType()).isEqualTo(0);
        assertThat(sessionOptional.get().getStatus()).isEqualTo("closed");
        assertThat(sessionOptional.get().getDateBegin()).isEqualTo(new Timestamp(dateFormat.parse("2016-08-18 18:25:07.115").getTime()));
        assertThat(sessionOptional.get().getDateEnd()).isEqualTo(new Timestamp(dateFormat.parse("2016-08-18 18:26:31.669").getTime()));
        assertThat(sessionOptional.get().getDateChanged()).isEqualTo(new Timestamp(dateFormat.parse("2016-08-18 18:26:31.669").getTime()));
        assertThat(sessionOptional.get().getDateVisited()).isEqualTo(new Timestamp(dateFormat.parse("2016-08-18 18:25:07.115").getTime()));
        assertThat(sessionOptional.get().getClientName()).isEqualTo("SBAC_PT");
        assertThat(sessionOptional.get().getProctorId()).isEqualTo(5);
        assertThat(sessionOptional.get().getBrowserKey()).isEqualTo(UUID.fromString("CB5C658D-4B32-463D-9DFA-119052E27474"));
    }

    @Test
    public void shouldReturnOptionalEmptyForInvalidSessionId() {
        UUID sessionId = UUID.randomUUID();
        Optional<Session> sessionOptional = sessionRepository.getSessionById(sessionId);

        assertThat(sessionOptional).isNotPresent();
    }
}
