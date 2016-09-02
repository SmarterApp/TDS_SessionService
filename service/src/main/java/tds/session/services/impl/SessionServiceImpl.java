package tds.session.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tds.session.Session;
import tds.session.repositories.SessionRepository;
import tds.session.services.SessionService;

import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Optional<Session> getSessionById(long id) {
        return sessionRepository.getSessionById(id);
    }
}
