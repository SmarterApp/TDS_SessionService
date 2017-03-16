package tds.session.services.impl;

import org.joda.time.Instant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import tds.common.Response;
import tds.common.ValidationError;
import tds.session.PauseSessionRequest;
import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.SessionAssessment;
import tds.session.error.ValidationErrorCode;
import tds.session.repositories.SessionAssessmentQueryRepository;
import tds.session.repositories.SessionRepository;
import tds.session.services.ExamService;
import tds.session.services.SessionService;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
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
        long proctorId = 1L;
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();

        Session mockOpenSession = new Session.Builder()
            .withId(sessionId)
            .withStatus("open")
            .withDateEnd(Instant.now().plus(600000))
            .withProctorId(proctorId)
            .withBrowserKey(browserKey)
            .build();

        // What the session will look like after mockSessionRepository.pause() is called
        Session mockUpdatedSession = new Session.Builder()
            .withId(sessionId)
            .withStatus("closed")
            .withDateChanged(Instant.now().plus(750000))
            .withDateEnd(Instant.now().plus(750000))
            .withProctorId(proctorId)
            .withBrowserKey(browserKey)
            .build();

        PauseSessionRequest request = new PauseSessionRequest(proctorId, browserKey);

        when(mockSessionRepository.findSessionById(mockOpenSession.getId()))
            .thenReturn(Optional.of(mockOpenSession))
            .thenReturn(Optional.of(mockUpdatedSession));
        doNothing().when(mockSessionRepository).pause(mockOpenSession.getId());
        doNothing().when(mockExamService).pauseAllExamsInSession(mockOpenSession.getId());

        Response<PauseSessionResponse> response = service.pause(mockOpenSession.getId(), request);

        verify(mockExamService).pauseAllExamsInSession(mockOpenSession.getId());
        verify(mockSessionRepository).pause(mockOpenSession.getId());
        verify(mockSessionRepository, times(2)).findSessionById(mockOpenSession.getId());

        assertThat(response.getData()).isNotNull();
        assertThat(response.hasError()).isFalse();
        PauseSessionResponse pauseSessionResponse = response.getData().get();
        assertThat(pauseSessionResponse.getStatus()).isEqualTo(mockUpdatedSession.getStatus());
        assertThat(pauseSessionResponse.getSessionId()).isEqualTo(mockUpdatedSession.getId());
        assertThat(pauseSessionResponse.getDateChanged()).isNotNull();
        assertThat(pauseSessionResponse.getDateEnded()).isNotNull();
    }

    @Test
    public void shouldRespondWithSessionClosedValidationErrorWhenPausingAClosedSession() {
        long proctorId = 1L;
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();

        Session mockClosedSession = new Session.Builder()
            .withId(sessionId)
            .withStatus("closed")
            .withDateChanged(Instant.now().minus(600000))
            .withDateEnd(Instant.now().minus(600000))
            .build();

        PauseSessionRequest request = new PauseSessionRequest(proctorId, browserKey);

        when(mockSessionRepository.findSessionById(mockClosedSession.getId())).thenReturn(Optional.of(mockClosedSession));

        Response<PauseSessionResponse> response = service.pause(sessionId, request);

        verifyZeroInteractions(mockExamService);
        verify(mockSessionRepository, times(1)).findSessionById(mockClosedSession.getId());
        verifyNoMoreInteractions(mockSessionRepository);

        assertThat(response.getData().isPresent()).isFalse();
        assertThat(response.hasError()).isTrue();
        ValidationError error = response.getError().get();
        assertThat(error.getCode()).isEqualTo(ValidationErrorCode.PAUSE_SESSION_IS_CLOSED);
        assertThat(error.getMessage()).isEqualTo("The session is closed");
    }

    @Test
    public void shouldRespondWithDifferentProctorValidationErrorWhenPausingASessionWithADifferentProctorId() {
        long proctorId = 1L;
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();

        Session mockClosedSession = new Session.Builder()
            .withId(sessionId)
            .withStatus("open")
            .withDateEnd(Instant.now().plus(600000))
            .withProctorId(5L)
            .build();

        PauseSessionRequest request = new PauseSessionRequest(proctorId, browserKey);

        when(mockSessionRepository.findSessionById(mockClosedSession.getId())).thenReturn(Optional.of(mockClosedSession));

        Response<PauseSessionResponse> response = service.pause(sessionId, request);

        verifyZeroInteractions(mockExamService);
        verify(mockSessionRepository, times(1)).findSessionById(mockClosedSession.getId());
        verifyNoMoreInteractions(mockSessionRepository);

        assertThat(response.getData().isPresent()).isFalse();
        assertThat(response.hasError()).isTrue();
        ValidationError error = response.getError().get();
        assertThat(error.getCode()).isEqualTo(ValidationErrorCode.PAUSE_SESSION_OWNED_BY_DIFFERENT_PROCTOR);
        assertThat(error.getMessage()).isEqualTo("The session is not owned by this proctor");
    }

    @Test
    public void shouldRespondWithAccessViolationValidationErrorWhenPausingASessionWithADifferentBrowserKey() {
        long proctorId = 1L;
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();

        Session mockClosedSession = new Session.Builder()
            .withId(sessionId)
            .withStatus("open")
            .withDateEnd(Instant.now().plus(600000))
            .withProctorId(proctorId)
            .withBrowserKey(UUID.randomUUID())
            .build();

        PauseSessionRequest request = new PauseSessionRequest(proctorId, browserKey);

        when(mockSessionRepository.findSessionById(mockClosedSession.getId())).thenReturn(Optional.of(mockClosedSession));
        doNothing().when(mockExamService).pauseAllExamsInSession(mockClosedSession.getId());

        Response<PauseSessionResponse> response = service.pause(sessionId, request);

        verify(mockExamService, times(0)).pauseAllExamsInSession(mockClosedSession.getId());
        verify(mockSessionRepository, times(0)).pause(mockClosedSession.getId());
        verify(mockSessionRepository, times(1)).findSessionById(mockClosedSession.getId());

        assertThat(response.getData().isPresent()).isFalse();
        assertThat(response.hasError()).isTrue();
        ValidationError error = response.getError().get();
        assertThat(error.getCode()).isEqualTo(ValidationErrorCode.PAUSE_SESSION_ACCESS_VIOLATION);
        assertThat(error.getMessage()).isEqualTo("Unauthorized session access");
    }

    @Test
    public void shouldUpdateSessionService() {
        final Session session = random(Session.class);

        when(mockSessionRepository.findSessionById(session.getId())).thenReturn(Optional.of(session));
        boolean successful = service.updateDateVisited(session.getId());

        assertThat(successful).isTrue();
        verify(mockSessionRepository).findSessionById(session.getId());
        verify(mockSessionRepository).updateDateVisited(session.getId());
    }

    @Test
    public void shouldReturnFalseForNoSessionFound() {
        final UUID sessionId = UUID.randomUUID();

        when(mockSessionRepository.findSessionById(sessionId)).thenReturn(Optional.empty());
        assertThat(service.updateDateVisited(sessionId)).isFalse();

        verify(mockSessionRepository).findSessionById(sessionId);
        verify(mockSessionRepository, never()).updateDateVisited(sessionId);
    }
}
