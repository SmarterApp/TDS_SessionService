package tds.session.services;

import tds.session.Session;

import java.util.Optional;
import java.util.UUID;

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
}

