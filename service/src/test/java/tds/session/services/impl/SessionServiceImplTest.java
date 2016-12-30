package tds.session.services.impl;

import org.joda.time.Instant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Optional;
import java.util.UUID;

import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.SessionAssessment;
import tds.session.repositories.SessionAssessmentQueryRepository;
import tds.session.repositories.SessionRepository;
import tds.session.services.ExamService;
import tds.session.services.SessionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionServiceImplTest {
    private SessionService service;

    @Mock
    private SessionRepository mockSessionRepository;

    @Mock
    private SessionAssessmentQueryRepository mockSessionAssessmentQueryRepository;

    @Mock
    private ExamService mockExamService;

    @Before
    public void setUp() {
        service = new SessionServiceImpl(mockSessionRepository, mockSessionAssessmentQueryRepository, mockExamService);
    }

    @After
    public void tearDown() {
    }

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

    @Test
    public void shouldPauseAllExamsInSession() {
        Session mockOpenSession = new Session.Builder()
            .withId(UUID.randomUUID())
            .withStatus("open")
            .withDateEnd(Instant.now().plus(600000))
            .build();

        // What the session will look like after mockSessionRepository.pause() is called
        Session mockUpdatedSession = new Session.Builder()
            .withId(mockOpenSession.getId())
            .withStatus("closed")
            .withDateChanged(Instant.now().plus(750000))
            .withDateEnd(Instant.now().plus(750000))
            .build();

        when(mockSessionRepository.findSessionById(mockOpenSession.getId()))
            .thenReturn(Optional.of(mockOpenSession))
            .thenReturn(Optional.of(mockUpdatedSession));
        doNothing().when(mockSessionRepository).pause(mockOpenSession.getId(), "closed");
        doNothing().when(mockExamService).pauseAllExamsInSession(mockOpenSession.getId());

        Optional<PauseSessionResponse> response = service.pause(mockOpenSession.getId(), "closed");

        verify(mockExamService).pauseAllExamsInSession(mockOpenSession.getId());
        verify(mockSessionRepository).pause(mockOpenSession.getId(), "closed");
        verify(mockSessionRepository, times(2)).findSessionById(mockOpenSession.getId());

        assertThat(response).isPresent();
        PauseSessionResponse pauseSessionResponse = response.get();
        assertThat(pauseSessionResponse.getStatus()).isEqualTo(mockUpdatedSession.getStatus());
        assertThat(pauseSessionResponse.getSessionId()).isEqualTo(mockUpdatedSession.getId());
        assertThat(pauseSessionResponse.getDateChanged()).isNotNull();
        assertThat(pauseSessionResponse.getDateEnded()).isNotNull();
    }

    @Test
    public void shouldNotCallExamServiceToPauseExamsOnAClosedSession() {
        Session mockClosedSession = new Session.Builder()
            .withId(UUID.randomUUID())
            .withStatus("closed")
            .withDateChanged(Instant.now().minus(600000))
            .withDateEnd(Instant.now().minus(600000))
            .build();

        when(mockSessionRepository.findSessionById(mockClosedSession.getId())).thenReturn(Optional.of(mockClosedSession));
        doNothing().when(mockExamService).pauseAllExamsInSession(mockClosedSession.getId());

        Optional<PauseSessionResponse> response = service.pause(mockClosedSession.getId(), "paused");

        verify(mockExamService, times(0)).pauseAllExamsInSession(mockClosedSession.getId());
        verify(mockSessionRepository, times(0)).pause(mockClosedSession.getId(), "paused");
        verify(mockSessionRepository, times(1)).findSessionById(mockClosedSession.getId());

        assertThat(response).isPresent();
        PauseSessionResponse pauseSessionResponse = response.get();
        assertThat(pauseSessionResponse.getStatus()).isEqualTo("closed");
        assertThat(pauseSessionResponse.getSessionId()).isEqualTo(mockClosedSession.getId());
        assertThat(pauseSessionResponse.getDateChanged()).isEqualTo(mockClosedSession.getDateChanged());
        assertThat(pauseSessionResponse.getDateEnded()).isEqualTo(mockClosedSession.getDateEnd());
    }
}
