package tds.session.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

import tds.common.web.exceptions.NotFoundException;
import tds.session.Session;
import tds.session.services.SessionService;
import tds.session.web.resources.SessionResource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionControllerTest {
    private SessionController controller;
    private SessionService sessionService;

    @Before
    public void setUp() {
        HttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        sessionService = mock(SessionService.class);
        controller = new SessionController(sessionService);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldFindASession() {
        UUID id = UUID.randomUUID();
        Session session = new Session.Builder()
                .withId(id)
                .build();
        when(sessionService.getSessionById(id)).thenReturn(Optional.of(session));

        ResponseEntity<SessionResource> response = controller.getSession(id);

        verify(sessionService).getSessionById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSession().getId()).isEqualTo(id);
        assertThat(response.getBody().getId().getHref()).isEqualTo("http://localhost/session/" + id.toString());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionForGettingAnInvalidSessionId() {
        UUID id = UUID.randomUUID();
        when(sessionService.getSessionById(id)).thenReturn(Optional.empty());
        controller.getSession(id);
    }
}
