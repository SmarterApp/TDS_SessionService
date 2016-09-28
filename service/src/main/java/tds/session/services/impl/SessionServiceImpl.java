package tds.session.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import tds.session.PauseSessionResponse;
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
    public Optional<Session> findSessionById(UUID id) {
        return sessionRepository.findSessionById(id);
    }

    @Override
    public Optional<PauseSessionResponse> pause(final UUID sessionId, final String newStatus) {
        final Optional<Session> sessionOptional = sessionRepository.findSessionById(sessionId);
        if (!sessionOptional.isPresent()) {
            return Optional.empty();
        }

        Session session = sessionOptional.get();
        PauseSessionResponse pauseSessionResponse = new PauseSessionResponse();

        // if session is not open, it does not need to be paused, so return.
        // TODO:  This condition might be over-simplified
        if (!session.isOpen()) {
            // TODO:  Collect all Exams associated w/the Session
            //pauseSessionResponse.setExamIds(examIds);
            pauseSessionResponse.setSession(session);
            return Optional.of(pauseSessionResponse);
        }

        // TODO:  Call pause exam for each Exam in session and set affected examIds list on response object
        // pauseSessionResponse.setExamIds();

        // Pause the Session
        sessionRepository.pause(sessionId, newStatus);

        // TODO:  Add call to create audit record that indicates the session is paused.
        Session updatedSession = sessionRepository.findSessionById(sessionId).get(); // we know the session exists already
        pauseSessionResponse.setSession(updatedSession);
        return Optional.of(pauseSessionResponse);
    }
}
