package tds.session.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import tds.session.Session;
import tds.session.repositories.SessionRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class SessionRepositoryImplIntegrationTests {

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    public void shouldRetrieveSessionForId() throws ParseException {
        UUID sessionId = UUID.fromString("06485031-B2B6-4CED-A0C1-B294EDA54DB2");

        Optional<Session> sessionOptional = sessionRepository.findSessionById(sessionId);
        assertThat(sessionOptional).isPresent();
        assertThat(sessionOptional.get().getId()).isEqualTo(sessionId);
        assertThat(sessionOptional.get().getType()).isEqualTo(0);
        assertThat(sessionOptional.get().getStatus()).isEqualTo("closed");
        assertThat(sessionOptional.get().getDateBegin()).isEqualTo(Instant.parse("2016-08-18T18:25:07.115Z"));
        assertThat(sessionOptional.get().getDateEnd()).isEqualTo(Instant.parse("2016-08-18T18:26:31.669Z"));
        assertThat(sessionOptional.get().getDateChanged()).isEqualTo(Instant.parse("2016-08-18T18:26:31.669Z"));
        assertThat(sessionOptional.get().getDateVisited()).isEqualTo(Instant.parse("2016-08-18T18:25:07.115Z"));
        assertThat(sessionOptional.get().getClientName()).isEqualTo("SBAC_PT");
        assertThat(sessionOptional.get().getProctorId()).isEqualTo(5);
        assertThat(sessionOptional.get().getBrowserKey()).isEqualTo(UUID.fromString("CB5C658D-4B32-463D-9DFA-119052E27474"));
    }

    @Test
    public void shouldReturnASessionForASessionIdThatHasNullDates() {
        UUID sessionId = UUID.fromString("A976E970-F80C-4107-830E-B1020053DE96");

        Optional<Session> sessionOptional = sessionRepository.findSessionById(sessionId);
        assertThat(sessionOptional).isPresent();
        assertThat(sessionOptional.get().getId()).isEqualTo(sessionId);
        assertThat(sessionOptional.get().getType()).isEqualTo(0);
        assertThat(sessionOptional.get().getStatus()).isEqualTo("open");
        assertThat(sessionOptional.get().getDateBegin()).isNull();
        assertThat(sessionOptional.get().getDateEnd()).isNull();
        assertThat(sessionOptional.get().getDateChanged()).isNull();
        assertThat(sessionOptional.get().getDateVisited()).isNull();
        assertThat(sessionOptional.get().getProctorId()).isEqualTo(6);
        assertThat(sessionOptional.get().getBrowserKey()).isEqualTo(UUID.fromString("F7A0375C-C63A-4164-976E-E883C2D13F62"));
    }

    @Test
    public void shouldHandleWhenSessionCannotBeFoundById() {
        Optional<Session> sessionOptional = sessionRepository.findSessionById(UUID.randomUUID());
        assertThat(sessionOptional).isNotPresent();
    }

    @Test
    public void shouldPauseASession() {
        UUID sessionId = UUID.fromString("08A57E3F-3A87-44C5-82A6-5B473E60785E");
        final String status = "unit test";  // represents the status change sent in from the caller

        sessionRepository.pause(sessionId, status);

        Optional<Session> result = sessionRepository.findSessionById(sessionId);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(sessionId);
        assertThat(result.get().getStatus()).isEqualTo(status);
        assertThat(result.get().getDateChanged()).isNotNull();
        assertThat(result.get().getDateChanged()).isGreaterThan(result.get().getDateBegin());
        assertThat(result.get().getDateEnd()).isNotNull();
        assertThat(result.get().getDateEnd()).isGreaterThan(result.get().getDateBegin());
    }
}
