package tds.session.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.SessionAssessment;
import tds.session.repositories.SessionAssessmentQueryRepository;
import tds.session.repositories.SessionRepository;
import tds.session.services.SessionService;

@Service
class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final SessionAssessmentQueryRepository sessionAssessmentQueryRepository;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository,
                              SessionAssessmentQueryRepository sessionAssessmentQueryRepository) {
        this.sessionRepository = sessionRepository;
        this.sessionAssessmentQueryRepository = sessionAssessmentQueryRepository;
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
        PauseSessionResponse pauseSessionResponse;

        // if session is not open, it does not need to be paused, so return.
        // TODO:  This condition might be over-simplified
        if (!session.isOpen()) {
            // TODO:  Collect all Exams associated w/the Session
            //pauseSessionResponse.setExamIds(examIds);
            pauseSessionResponse = new PauseSessionResponse(session);
            return Optional.of(pauseSessionResponse);
        }

        // TODO:  Call pause exam for each Exam in session and set affected examIds list on response object
        // pauseSessionResponse.setExamIds();

        // Pause the Session
        sessionRepository.pause(sessionId, newStatus);

        // TODO:  Add call to create audit record that indicates the session is paused.
        Session updatedSession = sessionRepository.findSessionById(sessionId).get(); // we know the session exists already
        pauseSessionResponse = new PauseSessionResponse(updatedSession);
        return Optional.of(pauseSessionResponse);
    }

    @Override
    public Optional<SessionAssessment> findSessionAssessment(UUID sessionId, String assessmentKey) {
        return sessionAssessmentQueryRepository.findSessionAssessment(sessionId, assessmentKey);
    }
}
