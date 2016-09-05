package tds.session.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tds.session.Session;
import tds.session.services.SessionService;
import tds.session.web.resources.SessionResource;
import web.exceptions.NotFoundException;

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
    public ResponseEntity<SessionResource> getSession(@PathVariable final long sessionId) {
        final Session session = sessionService.getSessionById(sessionId)
                .orElseThrow(() -> new NotFoundException("Could not find session for %d", sessionId));

        return ResponseEntity.ok(new SessionResource(session));
    }
}
