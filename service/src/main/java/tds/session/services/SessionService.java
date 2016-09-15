package tds.session.services;

import java.util.Optional;
import java.util.UUID;

import tds.session.Session;

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
    Optional<Session> getSessionById(UUID id);

    /**
     * Pause a session, updating it to the specified status.
     * <p>
     *     In the context of TDS, "paused" means the session has been stopped and can no longer support students taking
     *     examps.  A session that is "stopped" or "closed" (e.g. by a proctor voluntarily ending the session) is
     *     considered "paused".
     * </p>
     *
     * @param sessionId The id of the {@link Session} to pause
     * @param newStatus A description of why the {@link Session} is being paused
     */
    void pause(final UUID sessionId, final String newStatus);
}

