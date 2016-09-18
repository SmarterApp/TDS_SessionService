package tds.session.repositories;

import java.util.Optional;
import java.util.UUID;

import tds.session.Extern;
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
}
