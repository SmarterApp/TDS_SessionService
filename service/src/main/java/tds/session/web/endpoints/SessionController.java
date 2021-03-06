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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import tds.common.Response;
import tds.common.web.exceptions.NotFoundException;
import tds.session.PauseSessionRequest;
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
    public SessionController(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Session> findSessionById(@PathVariable final UUID sessionId) {
        final Session session = sessionService.findSessionById(sessionId)
            .orElseThrow(() -> new NotFoundException("Could not find session for %s", sessionId));

        return ResponseEntity.ok(session);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<Session>> findSessionsByIds(@RequestParam("sessionId") final UUID... sessionIds) {
        return ResponseEntity.ok(sessionService.findSessionsByIds(sessionIds));
    }

    @RequestMapping(value = "/{sessionId}/pause", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Response<PauseSessionResponse>> pause(@PathVariable final UUID sessionId, @RequestBody final PauseSessionRequest request) {
        final Response<PauseSessionResponse> response = sessionService.pause(sessionId, request);

        if (response.hasError()) {
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        URI location = linkTo(methodOn(SessionController.class).findSessionById(response.getData().get().getSessionId())).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{sessionId}/extend", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> extendSession(@PathVariable final UUID sessionId) {
        Boolean successful = sessionService.updateDateVisited(sessionId);

        if (!successful) {
            return new ResponseEntity<>(successful, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return ResponseEntity.ok(successful);
    }

    @RequestMapping(value = "/{sessionId}/assessment/{assessmentKey}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<SessionAssessment> findSessionAssessment(@PathVariable final UUID sessionId, @PathVariable final String assessmentKey) {
        final SessionAssessment sessionAssessment = sessionService.findSessionAssessment(sessionId, assessmentKey)
            .orElseThrow(() -> new NotFoundException("Could not find session assessment for %s and %s", sessionId, assessmentKey));

        return ResponseEntity.ok(sessionAssessment);
    }

    @RequestMapping(value = "/{sessionId}/assessment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<SessionAssessment>> findSessionAssessments(@PathVariable final UUID sessionId) {
        return ResponseEntity.ok(sessionService.findSessionAssessments(sessionId));
    }
}
