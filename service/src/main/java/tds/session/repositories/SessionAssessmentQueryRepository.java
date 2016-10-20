package tds.session.repositories;

import java.util.Optional;
import java.util.UUID;

import tds.session.SessionAssessment;

/**
 * Handles interaction with the session assessments
 */
public interface SessionAssessmentQueryRepository {
    /**
     * Finds the associated {@link tds.session.SessionAssessment}
     *
     * @param sessionId the session id
     * @return {@link tds.session.SessionAssessment} if found otherwise empty
     */
    Optional<SessionAssessment> findSessionAssessment(UUID sessionId);
}
