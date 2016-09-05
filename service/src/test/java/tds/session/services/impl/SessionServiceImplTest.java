package tds.session.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tds.session.Session;
import tds.session.repositories.SessionRepository;
import tds.session.services.SessionService;

import java.util.Optional;
import java.util.UUID;

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
        when(repository.getSessionById(id)).thenReturn(Optional.of(new Session(id, 0)));

        Optional<Session> session = service.getSessionById(id);

        assertThat(session.get().getId()).isEqualTo(id);

        verify(repository).getSessionById(id);
    }
}
