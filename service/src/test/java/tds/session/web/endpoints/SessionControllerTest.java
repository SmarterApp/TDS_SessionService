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

package tds.session.web.endpoints;

import org.joda.time.Instant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

import tds.common.Response;
import tds.common.web.exceptions.NotFoundException;
import tds.session.PauseSessionRequest;
import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.SessionAssessment;
import tds.session.services.SessionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionControllerTest {
    private SessionController controller;

    @Mock
    private SessionService mockSessionService;

    @Before
    public void setUp() {
        HttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        controller = new SessionController(mockSessionService);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldFindASessionById() {
        UUID id = UUID.randomUUID();
        Session session = new Session.Builder()
            .withId(id)
            .build();
        when(mockSessionService.findSessionById(id)).thenReturn(Optional.of(session));

        ResponseEntity<Session> response = controller.findSessionById(id);

        verify(mockSessionService).findSessionById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(id);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenSessionCannotBeFoundById() {
        UUID id = UUID.randomUUID();
        when(mockSessionService.findSessionById(id)).thenReturn(Optional.empty());
        controller.findSessionById(id);
    }

    @Test
    public void shouldPauseSession() {
        final UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();
        long proctorId = 1L;
        PauseSessionRequest request = new PauseSessionRequest(proctorId, browserKey);
        Session mockClosedSession = new Session.Builder()
            .withId(sessionId)
            .withProctorId(proctorId)
            .withBrowserKey(browserKey)
            .withDateChanged(Instant.now().minus(1000))
            .withDateEnd(Instant.now())
            .withStatus("closed")
            .build();
        PauseSessionResponse mockClosedResponse = new PauseSessionResponse(mockClosedSession);

        when(mockSessionService.pause(sessionId, request)).thenReturn(new Response<>(mockClosedResponse));
        ResponseEntity<Response<PauseSessionResponse>> responseEntity = controller.pause(sessionId, request);
        verify(mockSessionService).pause(sessionId, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        PauseSessionResponse resultFromService = responseEntity.getBody().getData().get();
        assertThat(resultFromService.getSessionId()).isEqualTo(sessionId);
        assertThat(resultFromService.getStatus()).isEqualTo("closed");
        assertThat(resultFromService.getDateEnded()).isEqualTo(mockClosedResponse.getDateEnded());
        assertThat(resultFromService.getDateChanged()).isEqualTo(mockClosedResponse.getDateChanged());
        assertThat(responseEntity.getHeaders().getLocation().toString()).isEqualTo("http://localhost/sessions/" + sessionId);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundIfSessionCannotBeFoundWhenPausingSession() {
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();
        long proctorId = 1L;
        PauseSessionRequest request = new PauseSessionRequest(proctorId, browserKey);

        when(mockSessionService.pause(sessionId, request)).thenThrow(new NotFoundException(String.format("Could not find session for session id %s", sessionId)));
        controller.pause(sessionId, request);
        verify(mockSessionService).pause(sessionId, request);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundIfSessionAssessmentNotFound() {
        UUID sessionId = UUID.randomUUID();
        when(mockSessionService.findSessionAssessment(sessionId, "")).thenReturn(Optional.empty());
        controller.findSessionAssessment(sessionId, "");
    }

    @Test
    public void shouldFindSessionAssessment() {
        UUID sessionId = UUID.randomUUID();
        SessionAssessment sessionAssessment = new SessionAssessment(sessionId, "ELA 3", "(SBAC) ELA 3");

        when(mockSessionService.findSessionAssessment(sessionId, "(SBAC) ELA 3")).thenReturn(Optional.of(sessionAssessment));
        ResponseEntity<SessionAssessment> response = controller.findSessionAssessment(sessionId, "(SBAC) ELA 3");
        verify(mockSessionService).findSessionAssessment(sessionId, "(SBAC) ELA 3");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sessionAssessment);
    }
}
