package tds.session.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tds.session.Session;
import tds.session.SessionServiceApplication;
import tds.session.services.SessionService;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class SessionServiceImplIntegrationTests {
    @Autowired
    private SessionService sessionService;

    @Test
    public void shouldPauseASession() {
        final UUID sessionId = UUID.fromString("08A57E3F-3A87-44C5-82A6-5B473E60785E");
        final String newStatus = "svc test";

        sessionService.pause(sessionId, newStatus);

        Optional<Session> result = sessionService.getSessionById(sessionId);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(sessionId);
        assertThat(result.get().getStatus()).isEqualTo(newStatus);
        assertThat(result.get().getDateChanged()).isNotNull();
        assertThat(result.get().getDateChanged()).isGreaterThan(result.get().getDateBegin());
        assertThat(result.get().getDateEnd()).isNotNull();
        assertThat(result.get().getDateEnd()).isGreaterThan(result.get().getDateBegin());
    }
}
