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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tds.common.Response;
import tds.common.ValidationError;
import tds.common.configuration.JacksonObjectMapperConfiguration;
import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;
import tds.session.PauseSessionRequest;
import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.SessionAssessment;
import tds.session.error.ValidationErrorCode;
import tds.session.services.SessionService;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SessionController.class)
@Import({ExceptionAdvice.class, JacksonObjectMapperConfiguration.class, SecurityConfiguration.class})
public class SessionControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private SessionService mockSessionService;

    @Autowired
    ObjectMapper objectMapper;

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
    public void shouldReturnSessionsByIds() throws Exception {
        final Session session1 = random(Session.class);
        final Session session2 = random(Session.class);

        when(mockSessionService.findSessionsByIds(session1.getId(), session2.getId())).thenReturn(Arrays.asList(session1, session2));

        MvcResult result = http.perform(get(new URI("/sessions/"))
            .contentType(MediaType.APPLICATION_JSON)
            .param("sessionId", session1.getId().toString())
            .param("sessionId", session2.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].id", is(session1.getId().toString())))
            .andExpect(jsonPath("[0].status", is(session1.getStatus())))
            .andExpect(jsonPath("[0].proctorName", is(session1.getProctorName())))
            .andExpect(jsonPath("[0].sessionKey", is(session1.getSessionKey())))
            .andExpect(jsonPath("[0].proctorId", is(session1.getProctorId())))
            .andExpect(jsonPath("[0].browserKey", is(session1.getBrowserKey().toString())))
            .andExpect(jsonPath("[1].id", is(session2.getId().toString())))
            .andExpect(jsonPath("[1].status", is(session2.getStatus())))
            .andExpect(jsonPath("[1].proctorName", is(session2.getProctorName())))
            .andExpect(jsonPath("[1].sessionKey", is(session2.getSessionKey())))
            .andExpect(jsonPath("[1].proctorId", is(session2.getProctorId())))
            .andExpect(jsonPath("[1].browserKey", is(session2.getBrowserKey().toString())))
            .andReturn();


        List<Session> parsedSessions = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<Session>>() {});
        assertThat(parsedSessions).hasSize(2);
        Session retSession1 = parsedSessions.get(0);
        Session retSession2 = parsedSessions.get(1);

        assertThat(retSession1.getId()).isEqualTo(session1.getId());
        assertThat(retSession2.getId()).isEqualTo(session2.getId());
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
    public void shouldPauseASession() throws Exception {
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();
        long proctorId = 1L;

        // What a session will look like after mockSessionService.pause() runs successfully
        Session mockClosedSession = new Session.Builder()
            .withId(sessionId)
            .withStatus("closed")
            .withBrowserKey(browserKey)
            .withProctorId(proctorId)
            .withDateChanged(Instant.now().plus(60000))
            .withDateEnd(Instant.now().plus(60000))
            .build();

        PauseSessionRequest request = new PauseSessionRequest(proctorId, browserKey);
        Response<PauseSessionResponse> mockResponse = new Response<>(new PauseSessionResponse(mockClosedSession));
        when(mockSessionService.pause(isA(UUID.class), isA(PauseSessionRequest.class))).thenReturn(mockResponse);

        http.perform(put(new URI(String.format("/sessions/%s/pause", mockClosedSession.getId())))
            .content(objectMapper.writer().writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("data.sessionId", is(mockClosedSession.getId().toString())))
            .andExpect(jsonPath("data.status", is("closed")))
            .andExpect(jsonPath("data.dateChanged").exists())
            .andExpect(jsonPath("data.dateEnded").exists());
    }

    @Test
    public void shouldReturnErrorsWhenASessionCannotBePaused() throws Exception {
        UUID sessionId = UUID.randomUUID();
        UUID browserKey = UUID.randomUUID();
        long proctorId = 1L;

        Session mockOwnedByAnotherProctor = new Session.Builder()
            .withId(sessionId)
            .withStatus("open")
            .withBrowserKey(browserKey)
            .withProctorId(5L)
            .withDateChanged(Instant.now().plus(60000))
            .withDateEnd(Instant.now().plus(60000))
            .build();

        PauseSessionRequest mockRequest = new PauseSessionRequest(proctorId, browserKey);
        ValidationError mockError = new ValidationError(ValidationErrorCode.PAUSE_SESSION_OWNED_BY_DIFFERENT_PROCTOR, "The session is not owned by this proctor");
        Response<PauseSessionResponse> mockResponse = new Response<>(new PauseSessionResponse(mockOwnedByAnotherProctor), mockError);
        when(mockSessionService.pause(isA(UUID.class), isA(PauseSessionRequest.class))).thenReturn(mockResponse);

        http.perform(put(new URI(String.format("/sessions/%s/pause", mockOwnedByAnotherProctor.getId())))
            .content(objectMapper.writer().writeValueAsString(mockRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("error.code", is("ownedByDifferentProctor")))
            .andExpect(jsonPath("error.message", is("The session is not owned by this proctor")));
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
    public void shouldReturnSessionAssessments() throws Exception {
        UUID sessionId = UUID.randomUUID();
        SessionAssessment sessionAssessment1 = random(SessionAssessment.class);
        SessionAssessment sessionAssessment2 = random(SessionAssessment.class);

        UriComponents components = UriComponentsBuilder
            .fromPath(String.format("/sessions/%s/assessment", sessionId)).build();

        when(mockSessionService.findSessionAssessments(sessionId)).thenReturn(Arrays.asList(sessionAssessment1, sessionAssessment2));

        MvcResult result = http.perform(get(components.toUri())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].sessionId", is(sessionAssessment1.getSessionId().toString())))
            .andExpect(jsonPath("[0].assessmentId", is(sessionAssessment1.getAssessmentId())))
            .andExpect(jsonPath("[0].assessmentKey", is(sessionAssessment1.getAssessmentKey())))
            .andExpect(jsonPath("[1].sessionId", is(sessionAssessment2.getSessionId().toString())))
            .andExpect(jsonPath("[1].assessmentId", is(sessionAssessment2.getAssessmentId())))
            .andExpect(jsonPath("[1].assessmentKey", is(sessionAssessment2.getAssessmentKey())))
            .andReturn();

        List<SessionAssessment> parsedResponse = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<SessionAssessment>>() {});
        assertThat(parsedResponse).hasSize(2);
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

    @Test
    public void shouldUpdateDateVisitedForSession() throws Exception {
        final UUID sessionId = UUID.randomUUID();
        final UriComponents components = UriComponentsBuilder
            .fromPath(String.format("/sessions/%s/extend", sessionId)).build();

        when(mockSessionService.updateDateVisited(sessionId)).thenReturn(true);

        http.perform(put(components.toUri())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(mockSessionService).updateDateVisited(sessionId);
    }

    @Test
    public void shouldReturnUnprocessableEntityForUnsuccessfulUpdate() throws Exception {
        final UUID sessionId = UUID.randomUUID();
        final UriComponents components = UriComponentsBuilder
            .fromPath(String.format("/sessions/%s/extend", sessionId)).build();

        when(mockSessionService.updateDateVisited(sessionId)).thenReturn(false);

        http.perform(put(components.toUri())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());

        verify(mockSessionService).updateDateVisited(sessionId);
    }
}
