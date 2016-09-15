package tds.session.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import tds.common.web.exceptions.NotFoundException;
import tds.session.Session;
import tds.session.services.SessionService;
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
    public ResponseEntity<Void> pause(@PathVariable final UUID sessionId, @RequestBody final String newStatus) {
        sessionService.pause(sessionId, newStatus);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(methodOn(SessionController.class).getSession(sessionId)).toUri());

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
