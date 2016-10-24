package tds.session.web.endpoints;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.SessionAssessment;
import tds.session.services.SessionService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SessionController.class)
public class SessionControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private SessionService mockSessionService;

    @Test
    public void shouldReturnSessionWhenFoundById() throws Exception {
        Session session = new Session.Builder()
            .withId(UUID.randomUUID())
            .withStatus("closed")
            .build();

        when(mockSessionService.findSessionById(session.getId())).thenReturn(Optional.of(session));

        http.perform(get(new URI(String.format("/sessions/%s", session.getId())))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(session.getId().toString())))
            .andExpect(jsonPath("status", is("closed")));
    }

    @Test
    public void shouldReturnNotFoundWhenSessionCannotBeFoundById() throws Exception {
        UUID id = UUID.randomUUID();
        when(mockSessionService.findSessionById(id)).thenReturn(Optional.empty());

        http.perform(get(new URI(String.format("/sessions/%s", id)))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void shouldHandleNonUUIDWhenFindingSessionById() throws Exception {
        http.perform(get(new URI("/sessions/invalid-uuid"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldPauseASession() throws Exception{
        UUID id = UUID.randomUUID();

        Session session = new Session.Builder()
            .withId(id)
            .withStatus("paused")
            .withClientName("SBAC_PT")
            .build();

        when(mockSessionService.pause(id, "paused")).thenReturn(Optional.of(new PauseSessionResponse(session)));

        http.perform(put(new URI(String.format("/sessions/%s/pause", session.getId())))
            .content("paused")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("sessionId", is(session.getId().toString())))
            .andExpect(jsonPath("status", is("paused")));
    }

    @Test
    public void shouldReturnSessionAssessment() throws Exception {
        UUID sessionId = UUID.randomUUID();
        SessionAssessment sessionAssessment = new SessionAssessment(sessionId, "ELA 11", "(SBAC) ELA 11");

        UriComponents components = UriComponentsBuilder
            .fromPath(String.format("/sessions/%s/assessment/%s", sessionId, "(SBAC) ELA 11")).build();

        when(mockSessionService.findSessionAssessment(sessionId, "(SBAC) ELA 11")).thenReturn(Optional.of(sessionAssessment));

        http.perform(get(components.toUri())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("sessionId", is(sessionId.toString())))
            .andExpect(jsonPath("assessmentId", is("ELA 11")))
            .andExpect(jsonPath("assessmentKey", is("(SBAC) ELA 11")));
    }

    @Test
    public void shouldReturnNotFoundWhenSessionAssessmentCannotBeFoundById() throws Exception {
        UUID id = UUID.randomUUID();
        when(mockSessionService.findSessionAssessment(id, "(SBAC) ELA 11")).thenReturn(Optional.empty());

        UriComponents components = UriComponentsBuilder
            .fromPath(String.format("/sessions/%s/assessment/%s", id, "(SBAC) ELA 11")).build();

        http.perform(get(components.toUri())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
