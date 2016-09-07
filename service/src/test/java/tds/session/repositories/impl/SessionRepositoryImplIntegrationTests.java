package tds.session.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    public void shouldRetrieveSessionForId() {
        UUID sessionId = UUID.fromString("06485031-B2B6-4CED-A0C1-B294EDA54DB2");
        Optional<Session> sessionOptional = sessionRepository.getSessionById(sessionId);
        assertThat(sessionOptional.isPresent()).isTrue();
        assertThat(sessionOptional.get().getId()).isEqualTo(sessionId);
        assertThat(sessionOptional.get().getStatus()).isEqualTo("closed");
        assertThat(sessionOptional.get().getType()).isEqualTo(0);
    }

}
