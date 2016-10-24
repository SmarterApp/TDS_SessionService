package tds.session;

import java.util.UUID;

/**
 * Assessment information for the session
 */
public class SessionAssessment {
    private UUID sessionId;
    private String assessmentId;
    private String assessmentKey;

    /**
     * @param sessionId     session id
     * @param assessmentId  the overall assessment id
     * @param assessmentKey the specific unique assessment key for the session assessment
     */
    public SessionAssessment(UUID sessionId, String assessmentId, String assessmentKey) {
        this.sessionId = sessionId;
        this.assessmentId = assessmentId;
        this.assessmentKey = assessmentKey;
    }

    /**
     * @return session id
     */
    public UUID getSessionId() {
        return sessionId;
    }

    /**
     * @return the assessment id
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    /**
     * @return unique key for a specific assessment
     */
    public String getAssessmentKey() {
        return assessmentKey;
    }
}
