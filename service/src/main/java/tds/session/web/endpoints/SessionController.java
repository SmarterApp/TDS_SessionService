package tds.session.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tds.session.Session;
import tds.session.services.SessionService;

import java.util.Optional;

/**
 * Contains the endpoints for the session
 */
@RestController
public class SessionController {
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/session/{sessionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Session> getSession(@PathVariable final long sessionId) {
        final Optional<Session> clientSystemFlag = sessionService.getSessionById(sessionId);
//        if (!clientSystemFlag.isPresent()) {
//            throw new NotFoundException("Could not find ClientSystemFlag for client name " + clientName + " and audit object " + auditObject);
//        }

        return ResponseEntity.ok(null);
    }
}
