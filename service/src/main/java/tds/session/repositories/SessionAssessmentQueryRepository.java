package tds.session.repositories;

import java.util.List;
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
    Optional<SessionAssessment> findSessionAssessment(final UUID sessionId, final String assessmentKey);

    /**
     * Finds the associated {@link List<tds.session.SessionAssessment>} for a session
     *
     * @param sessionId the id of the session to find the {@link tds.session.SessionAssessment}s for
     * @return the list of all {@link tds.session.SessionAssessment}s for this session
     */
    List<SessionAssessment> findSessionAssessments(final UUID sessionId);
}
