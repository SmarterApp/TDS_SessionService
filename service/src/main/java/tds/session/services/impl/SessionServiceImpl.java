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
}
