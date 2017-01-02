package tds.session.services;

import java.util.Optional;
import java.util.UUID;

import tds.common.Response;
import tds.session.PauseSessionRequest;
import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.SessionAssessment;

/**
 * Handles interactions with the testing session
 */
public interface SessionService {
    /**
     * Retrieve the {@link tds.session.Session Session} for the given id
     *
     * @param id session id
     * @return Optional containing {@link tds.session.Session Session} otherwise empty
     */
    Optional<Session> findSessionById(UUID id);

    /**
     * Pause a session, updating it to the specified status.
     * <p>
     * In the context of TDS, "paused" means the session has been stopped and can no longer support students taking
     * exams.  A session that is "stopped" or "closed" (e.g. by a proctor voluntarily ending the session) is
     * considered "paused".
     * </p>
     *
     * @param sessionId he session id of {@link tds.session.Session} should be paused
     * @param request   The proctor id and browser key to identify the requester
     */
    Response<PauseSessionResponse> pause(UUID sessionId, PauseSessionRequest request);

    /**
     * Finds the associated {@link tds.session.SessionAssessment}
     *
     * @param sessionId     the session id
     * @param assessmentKey the unique identifier for a particular assessment
     * @return {@link tds.session.SessionAssessment} if found otherwise empty
     */
    Optional<SessionAssessment> findSessionAssessment(UUID sessionId, String assessmentKey);
}

