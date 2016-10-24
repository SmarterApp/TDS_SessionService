package tds.session.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import tds.session.Session;
import tds.session.SessionAssessment;
import tds.session.repositories.SessionAssessmentQueryRepository;
import tds.session.repositories.SessionRepository;
import tds.session.services.SessionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionServiceImplTest {
    private SessionService service;

    @Mock
    private SessionRepository mockSessionRepository;

    @Mock
    private SessionAssessmentQueryRepository mockSessionAssessmentQueryRepository;

    @Before
    public void setUp() {
        service = new SessionServiceImpl(mockSessionRepository, mockSessionAssessmentQueryRepository);
    }

    @After
    public void tearDown(){}

    @Test
    public void shouldReturnASession() {
        UUID id = UUID.randomUUID();
        Session session = new Session.Builder()
                .withId(id)
                .build();
        when(mockSessionRepository.findSessionById(id)).thenReturn(Optional.of(session));

        Optional<Session> sessionOptional = service.findSessionById(id);

        assertThat(sessionOptional).isPresent();
        assertThat(sessionOptional.get().getId()).isEqualTo(id);

        verify(mockSessionRepository).findSessionById(id);
    }

    @Test
    public void shouldReturnOptionalEmptyForInvalidSessionId() {
        UUID id = UUID.randomUUID();
        when(mockSessionRepository.findSessionById(id)).thenReturn(Optional.empty());

        Optional<Session> result = service.findSessionById(id);

        assertThat(result).isNotPresent();
    }

    @Test
    public void shouldReturnSessionAssessment() {
        UUID sessionId = UUID.randomUUID();
        SessionAssessment sessionAssessment = new SessionAssessment(sessionId, "3 ELA", "(SBAC) 3 ELA 2015 - 2016");
        when(mockSessionAssessmentQueryRepository.findSessionAssessment(sessionId, "(SBAC) 3 ELA 2015 - 2016")).thenReturn(Optional.of(sessionAssessment));

        Optional<SessionAssessment> maybeSessionAssessment = service.findSessionAssessment(sessionId, "(SBAC) 3 ELA 2015 - 2016");

        verify(mockSessionAssessmentQueryRepository).findSessionAssessment(sessionId, "(SBAC) 3 ELA 2015 - 2016");

        assertThat(maybeSessionAssessment).isPresent();
    }
}
