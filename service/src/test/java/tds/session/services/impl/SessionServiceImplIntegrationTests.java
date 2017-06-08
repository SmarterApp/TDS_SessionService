package tds.session.services.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
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
@TestPropertySource(properties = "tds.cache.enabled = true")
public class SessionServiceImplIntegrationTests {
    @MockBean
    private SessionRepository mockSessionRepository;

    @Autowired
    private SessionService sessionService;

    @Test
    @Ignore("Session caching is disabled until we can handle eviction")
    public void shouldReturnCachedSession() {
        UUID id = UUID.randomUUID();
        Session session = new Session.Builder()
            .withId(id)
            .build();
        when(mockSessionRepository.findSessionsByIds(id)).thenReturn(Collections.singletonList(session));

        Optional<Session> sessionOptional1 = sessionService.findSessionById(id);
        Optional<Session> sessionOptional2 = sessionService.findSessionById(id);

        assertThat(sessionOptional1).isPresent();
        assertThat(sessionOptional1).isEqualTo(sessionOptional2);

        verify(mockSessionRepository, times(1)).findSessionsByIds(id);
    }
}
