package tds.session.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import tds.common.web.exceptions.NotFoundException;
import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.services.SessionService;
import tds.session.web.resources.PauseSessionResponseResource;
import tds.session.web.resources.SessionResource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Contains the endpoints for the session
 */
@RestController
@RequestMapping("/session")
public class SessionController {
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<SessionResource> getSession(@PathVariable final UUID sessionId) {
        final Session session = sessionService.getSessionById(sessionId)
                .orElseThrow(() -> new NotFoundException("Could not find session for %s", sessionId));

        return ResponseEntity.ok(new SessionResource(session));
    }

    @RequestMapping(value = "/{sessionId}/pause", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PauseSessionResponseResource> pause(@PathVariable final UUID sessionId, @RequestBody final String newStatus) {
        final PauseSessionResponse response = sessionService.pause(sessionId, newStatus)
                .orElseThrow(() -> new NotFoundException("Could not find session id %s", sessionId));

        final PauseSessionResponseResource resource = new PauseSessionResponseResource(response);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(resource.getLink("session").getHref()));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
