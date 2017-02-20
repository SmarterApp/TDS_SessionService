package tds.session.services;

import java.util.UUID;

/**
 * Handles interaction with the exam service
 */
public interface ExamService {
    /**
     * Update the status of all exams in the specified {@link tds.session.Session} to "paused"
     *
     * @param sessionId The unique identifier of the session that has been closed
     */
    void pauseAllExamsInSession(final UUID sessionId);
}
