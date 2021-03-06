/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.session.services.impl;

import org.joda.time.Instant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        when(mockSessionRepository.findSessionsByIds(id)).thenReturn(Collections.singletonList(session));

        Optional<Session> sessionOptional = service.findSessionById(id);

        assertThat(sessionOptional).isPresent();
        assertThat(sessionOptional.get().getId()).isEqualTo(id);

        verify(mockSessionRepository).findSessionsByIds(id);
    }

    @Test
    public void shouldReturnOptionalEmptyForInvalidSessionId() {
        UUID id = UUID.randomUUID();
        when(mockSessionRepository.findSessionsByIds(id)).thenReturn(new ArrayList<>());

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
    public void shouldFindAllSessionAssessmentsForSession() {
        final UUID sessionId = UUID.randomUUID();
        final SessionAssessment sessionAssessment1 = random(SessionAssessment.class);
        final SessionAssessment sessionAssessment2 = random(SessionAssessment.class);

        when(mockSessionAssessmentQueryRepository.findSessionAssessments(sessionId))
            .thenReturn(Arrays.asList(sessionAssessment1, sessionAssessment2));
        List<SessionAssessment> sessionAssessments = service.findSessionAssessments(sessionId);
        verify(mockSessionAssessmentQueryRepository).findSessionAssessments(sessionId);

        assertThat(sessionAssessments).containsExactlyInAnyOrder(sessionAssessment1, sessionAssessment2);
    }

    @Test
    public void shouldFindAllSessionsForSessionId() {
        Session session1 = random(Session.class);
        Session session2 = random(Session.class);

        when(mockSessionRepository.findSessionsByIds(session1.getId(), session2.getId()))
            .thenReturn(Arrays.asList(session1, session2));
        List<Session> retSessions = service.findSessionsByIds(session1.getId(), session2.getId());

        Session retSession1 = null;
        Session retSession2 = null;

        for (Session s : retSessions) {
            if (s.getId().equals(session1.getId())) {
                retSession1 = s;
            } else if (s.getId().equals(session2.getId())) {
                retSession2 = s;
            }
        }

        assertThat(retSession1.getId()).isEqualTo(session1.getId());
        assertThat(retSession1.getStatus()).isEqualTo(session1.getStatus());
        assertThat(retSession1.getDateBegin()).isEqualByComparingTo(session1.getDateBegin());
        assertThat(retSession1.getDateChanged()).isEqualTo(session1.getDateChanged());
        assertThat(retSession1.getDateEnd()).isEqualTo(session1.getDateEnd());
        assertThat(retSession1.getDateVisited()).isEqualTo(session1.getDateVisited());
        assertThat(retSession1.getClientName()).isEqualTo(session1.getClientName());
        assertThat(retSession1.getProctorId()).isEqualTo(session1.getProctorId());
        assertThat(retSession1.getBrowserKey()).isEqualTo(session1.getBrowserKey());
        assertThat(retSession1.getProctorName()).isEqualTo(session1.getProctorName());
        assertThat(retSession1.getProctorEmail()).isEqualTo(session1.getProctorEmail());

        assertThat(retSession2.getId()).isEqualTo(session2.getId());
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

        when(mockSessionRepository.findSessionsByIds(mockOpenSession.getId()))
            .thenReturn(Collections.singletonList(mockOpenSession))
            .thenReturn(Collections.singletonList(mockUpdatedSession));
        doNothing().when(mockSessionRepository).pause(mockOpenSession.getId());
        doNothing().when(mockExamService).pauseAllExamsInSession(mockOpenSession.getId());

        Response<PauseSessionResponse> response = service.pause(mockOpenSession.getId(), request);

        verify(mockExamService).pauseAllExamsInSession(mockOpenSession.getId());
        verify(mockSessionRepository).pause(mockOpenSession.getId());
        verify(mockSessionRepository, times(2)).findSessionsByIds(mockOpenSession.getId());

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

        when(mockSessionRepository.findSessionsByIds(mockClosedSession.getId())).thenReturn(Collections.singletonList(mockClosedSession));

        Response<PauseSessionResponse> response = service.pause(sessionId, request);

        verifyZeroInteractions(mockExamService);
        verify(mockSessionRepository, times(1)).findSessionsByIds(mockClosedSession.getId());
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

        when(mockSessionRepository.findSessionsByIds(mockClosedSession.getId())).thenReturn(Collections.singletonList(mockClosedSession));

        Response<PauseSessionResponse> response = service.pause(sessionId, request);

        verifyZeroInteractions(mockExamService);
        verify(mockSessionRepository, times(1)).findSessionsByIds(mockClosedSession.getId());
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

        when(mockSessionRepository.findSessionsByIds(mockClosedSession.getId())).thenReturn(Collections.singletonList(mockClosedSession));
        doNothing().when(mockExamService).pauseAllExamsInSession(mockClosedSession.getId());

        Response<PauseSessionResponse> response = service.pause(sessionId, request);

        verify(mockExamService, times(0)).pauseAllExamsInSession(mockClosedSession.getId());
        verify(mockSessionRepository, times(0)).pause(mockClosedSession.getId());
        verify(mockSessionRepository, times(1)).findSessionsByIds(mockClosedSession.getId());

        assertThat(response.getData().isPresent()).isFalse();
        assertThat(response.hasError()).isTrue();
        ValidationError error = response.getError().get();
        assertThat(error.getCode()).isEqualTo(ValidationErrorCode.PAUSE_SESSION_ACCESS_VIOLATION);
        assertThat(error.getMessage()).isEqualTo("Unauthorized session access");
    }

    @Test
    public void shouldUpdateSessionService() {
        final Session session = random(Session.class);

        when(mockSessionRepository.findSessionsByIds(session.getId())).thenReturn(Collections.singletonList(session));
        boolean successful = service.updateDateVisited(session.getId());

        assertThat(successful).isTrue();
        verify(mockSessionRepository).findSessionsByIds(session.getId());
        verify(mockSessionRepository).updateDateVisited(session.getId());
    }

    @Test
    public void shouldReturnFalseForNoSessionFound() {
        final UUID sessionId = UUID.randomUUID();

        when(mockSessionRepository.findSessionsByIds(sessionId)).thenReturn(new ArrayList<>());
        assertThat(service.updateDateVisited(sessionId)).isFalse();

        verify(mockSessionRepository).findSessionsByIds(sessionId);
        verify(mockSessionRepository, never()).updateDateVisited(sessionId);
    }
}
