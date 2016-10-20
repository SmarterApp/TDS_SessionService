package tds.session.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

import tds.common.web.exceptions.NotFoundException;
import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.SessionAssessment;
import tds.session.services.SessionService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Contains the endpoints for the session
 */
@RestController
@RequestMapping("/sessions")
class SessionController {
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Session> findSessionById(@PathVariable final UUID sessionId) {
        final Session session = sessionService.findSessionById(sessionId)
                .orElseThrow(() -> new NotFoundException("Could not find session for %s", sessionId));

        return ResponseEntity.ok(session);
    }

    @RequestMapping(value = "/{sessionId}/pause", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<PauseSessionResponse> pause(@PathVariable final UUID sessionId, @RequestBody final String newStatus) {
        final PauseSessionResponse response = sessionService.pause(sessionId, newStatus)
                .orElseThrow(() -> new NotFoundException("Could not find session id %s", sessionId));


        URI location = linkTo(methodOn(SessionController.class).findSessionById(response.getSessionId())).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{sessionId}/assessment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<SessionAssessment> findSessionAssessment(@PathVariable final UUID sessionId) {
        final SessionAssessment sessionAssessment = sessionService.findSessionAssessment(sessionId)
            .orElseThrow(() -> new NotFoundException("Could not find session assessment for %s", sessionId));

        return ResponseEntity.ok(sessionAssessment);
    }
}
