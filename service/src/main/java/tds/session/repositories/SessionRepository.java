package tds.session.repositories;

import java.util.Optional;
import java.util.UUID;

import tds.session.Session;

/**
 * Handles the data access for {@link tds.session.Session Session}
 */
public interface SessionRepository {
    /**
     * Fetches the Session by given id
     * @param id session id
     * @return optional containing {@link tds.session.Session Session} otherwise empty Optional
     */
    Optional<Session> getSessionById(UUID id);

    /**
     * Pause an existing {@link Session}, updating the {@link Session}'s status to indicate it is no longer "open".
     * <p>
     *     The database design for microservices is to never execute an UPDATE against existing records.  Any changes
     *     to the state of an object should be recorded as a new record.  For example, when a session is paused, its
     *     state is changed.  Rather than updating the existing record in the session.session table, a new record should
     *     be inserted into a newStatus table that records the change.
     * </p>
     *
     * @param sessionId The id of the {@link Session} to pause
     * @param newStatus A description of why the {@link Session} is being paused
     */
    void pause(final UUID sessionId, final String newStatus);
}
