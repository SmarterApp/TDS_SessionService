package tds.session.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import tds.session.Session;
import tds.session.repositories.SessionRepository;
import tds.session.services.SessionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionServiceImplTest {
    private SessionRepository repository;
    private SessionService service;

    @Before
    public void setUp() {
        repository = mock(SessionRepository.class);
        service = new SessionServiceImpl(repository);
    }

    @After
    public void tearDown(){}

    @Test
    public void itShouldReturnASession() {
        UUID id = UUID.randomUUID();
        Session session = new Session();
        session.setId(id);
        when(repository.getSessionById(id)).thenReturn(Optional.of(session));

        Optional<Session> sessionOptional = service.getSessionById(id);

        assertThat(sessionOptional.get().getId()).isEqualTo(id);

        verify(repository).getSessionById(id);
    }
}
