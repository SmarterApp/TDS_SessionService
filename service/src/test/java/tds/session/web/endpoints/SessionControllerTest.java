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

import tds.common.web.exceptions.NotFoundException;
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
        final String newStatus = "paused";
        final Instant dateChanged = Instant.now();
        Session session = new Session.Builder()
            .withId(sessionId)
            .withStatus(newStatus)
            .withDateChanged(dateChanged)
            .build();

        PauseSessionResponse pauseSessionResponse = new PauseSessionResponse(session);

        when(mockSessionService.pause(sessionId, newStatus)).thenReturn(Optional.of(pauseSessionResponse));
        ResponseEntity<PauseSessionResponse> responseEntity = controller.pause(sessionId, newStatus);
        verify(mockSessionService).pause(sessionId, newStatus);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getExamIds()).isEqualTo(pauseSessionResponse.getExamIds());
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(newStatus);
        assertThat(responseEntity.getBody().getDateChanged()).isEqualTo(dateChanged);
        assertThat(responseEntity.getHeaders().getLocation().toString()).isEqualTo("http://localhost/sessions/" + sessionId);
    }

    @Test (expected = NotFoundException.class)
    public void shouldThrowNotFoundIfSessionCannotBeFoundWhenPausingSession() {
        UUID sessionId = UUID.randomUUID();
        when(mockSessionService.pause(sessionId, "paused")).thenReturn(Optional.empty());
        controller.pause(sessionId, "paused");
    }

    @Test (expected = NotFoundException.class)
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
