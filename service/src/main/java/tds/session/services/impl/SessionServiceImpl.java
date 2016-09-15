package tds.session.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import tds.session.Session;
import tds.session.repositories.SessionRepository;
import tds.session.services.SessionService;

@Service
class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Optional<Session> getSessionById(UUID id) {
        return sessionRepository.getSessionById(id);
    }

    @Override
    public void pause(final UUID sessionId, final String newStatus) {
        // TODO:  Throw an exception if attempting to pause a Session that is already paused?
        sessionRepository.pause(sessionId, newStatus);
        // TODO:  Add call to create audit record that indicates the session is paused.
    }
}
