package tds.session.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.UUID;

import tds.session.Session;
import tds.session.repositories.SessionRepository;
import tds.session.services.SessionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SessionServiceImplIntegrationTests {
    @MockBean
    private SessionRepository mockSessionRepository;

    @Autowired
    private SessionService sessionService;

    @Test
    public void shouldReturnCachedSession() {
        UUID id = UUID.randomUUID();
        Session session = new Session.Builder()
            .withId(id)
            .build();
        when(mockSessionRepository.findSessionById(id)).thenReturn(Optional.of(session));

        Optional<Session> sessionOptional1 = sessionService.findSessionById(id);
        Optional<Session> sessionOptional2 = sessionService.findSessionById(id);

        assertThat(sessionOptional1).isPresent();
        assertThat(sessionOptional1).isEqualTo(sessionOptional2);

        verify(mockSessionRepository, times(1)).findSessionById(id);
    }
}
