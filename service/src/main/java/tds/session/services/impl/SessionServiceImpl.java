package tds.session.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tds.common.Response;
import tds.common.ValidationError;
import tds.common.cache.CacheType;
import tds.common.web.exceptions.NotFoundException;
import tds.session.PauseSessionRequest;
import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.SessionAssessment;
import tds.session.error.ValidationErrorCode;
import tds.session.repositories.SessionAssessmentQueryRepository;
import tds.session.repositories.SessionRepository;
import tds.session.services.ExamService;
import tds.session.services.SessionService;

@Service
class SessionServiceImpl implements SessionService {
    private static final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);
    private final SessionRepository sessionRepository;
    private final SessionAssessmentQueryRepository sessionAssessmentQueryRepository;
    private final ExamService examService;

    @Autowired
    public SessionServiceImpl(final SessionRepository sessionRepository,
                              final SessionAssessmentQueryRepository sessionAssessmentQueryRepository,
                              final ExamService examService) {
        this.sessionRepository = sessionRepository;
        this.sessionAssessmentQueryRepository = sessionAssessmentQueryRepository;
        this.examService = examService;
    }

    @Override
    @Cacheable(CacheType.SHORT_TERM)
    public Optional<Session> findSessionById(final UUID id) {
        return sessionRepository.findSessionsByIds(id).stream().findFirst();
    }

    @Transactional
    @Override
    public Response<PauseSessionResponse> pause(final UUID sessionId, final PauseSessionRequest request) {
        final Session session = findSessionById(sessionId)
            .orElseThrow(() -> new NotFoundException(String.format("Could not find session for session id %s", sessionId)));

        Optional<ValidationError> maybeValidationError = verifySessionCanBePaused(session, request);
        if (maybeValidationError.isPresent()) {
            return new Response<>(maybeValidationError.get());
        }

        examService.pauseAllExamsInSession(sessionId);
        sessionRepository.pause(sessionId);

        Session updatedSession = findSessionById(sessionId)
            .orElseThrow(() -> new IllegalStateException(String.format("Could not find session that was just closed for session id %s", sessionId)));

        return new Response<>(new PauseSessionResponse(updatedSession));
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
    public Optional<SessionAssessment> findSessionAssessment(final UUID sessionId, final String assessmentKey) {
        return sessionAssessmentQueryRepository.findSessionAssessment(sessionId, assessmentKey);
    }

    @Override
    public boolean updateDateVisited(final UUID sessionId) {
        Optional<Session> maybeSession = findSessionById(sessionId);

        if (!maybeSession.isPresent()) {
            log.error("No session for session id {} found. Unable to extend session.", sessionId);
            return false;
        }

        sessionRepository.updateDateVisited(sessionId);

        return true;
    }

    @Override
    public List<SessionAssessment> findSessionAssessments(final UUID sessionId) {
        return sessionAssessmentQueryRepository.findSessionAssessments(sessionId);
    }

    @Override
    public List<Session> findSessionsByIds(final UUID... sessionIds) {
        return sessionRepository.findSessionsByIds(sessionIds);
    }

    /**
     * Determine if the requested {@link tds.session.Session} can be paused.
     *
     * @param session The session being paused
     * @param request The request submitted to pause the session
     * @return {@code Optional.empty()} if the session can be paused, otherwise a {@link tds.common.ValidationError}
     * that describes why the session cannot be paused.
     */
    private Optional<ValidationError> verifySessionCanBePaused(final Session session, final PauseSessionRequest request) {
        // CommonDLL.ValidateProctorSession_FN (@ line 1727) rules:

        // RULE:  The session must be open
        if (!session.isOpen()) {
            return Optional.of(new ValidationError(ValidationErrorCode.PAUSE_SESSION_IS_CLOSED, "The session is closed"));
        }

        // RULE:  The Proctor making the request must own the session
        if (session.getProctorId() != request.getProctorId()) {
            return Optional.of(new ValidationError(ValidationErrorCode.PAUSE_SESSION_OWNED_BY_DIFFERENT_PROCTOR, "The session is not owned by this proctor"));
        }

        // RULE:  The requester's browser key must match the session's browser key
        if (!session.getBrowserKey().equals(request.getBrowserKey())) {
            return Optional.of(new ValidationError(ValidationErrorCode.PAUSE_SESSION_ACCESS_VIOLATION, "Unauthorized session access"));
        }

        return Optional.empty();
    }
}
