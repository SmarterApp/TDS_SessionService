package tds.session.repositories;

import java.util.List;
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
     * @return list {@link tds.session.SessionAssessment}
     */
    List<SessionAssessment> findSessionAssessment(UUID sessionId);
}
