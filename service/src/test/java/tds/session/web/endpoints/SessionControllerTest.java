package tds.session.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tds.session.Session;
import tds.session.services.SessionService;
import tds.session.web.resources.SessionResource;
import web.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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
    public void tearDown() {}

    @Test
    public void aSessionCanBeFound() {
        when(sessionService.getSessionById(1L)).thenReturn(Optional.of(new Session(0, 1L)));

        ResponseEntity<SessionResource> response = controller.getSession(1L);

        verify(sessionService).getSessionById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSession().getId()).isEqualTo(1L);
        assertThat(response.getBody().getId().getHref()).isEqualTo("http://localhost/session/1");
    }

    @Test (expected = NotFoundException.class)
    public void sessionNotFoundThrows() {
        when(sessionService.getSessionById(1L)).thenReturn(Optional.empty());
        controller.getSession(1L);
    }
}
