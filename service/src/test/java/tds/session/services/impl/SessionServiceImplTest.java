package tds.session.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tds.session.Session;
import tds.session.repositories.SessionRepository;
import tds.session.services.SessionService;

import java.util.Optional;

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
        when(repository.getSessionById(1L)).thenReturn(Optional.of(new Session(0, 1L)));

        Optional<Session> session = service.getSessionById(1L);

        assertThat(session.get().getId()).isEqualTo(1L);

        verify(repository).getSessionById(1L);
    }
}
