package tds.session;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A response indicating the {@link Session} that was paused along with its associated Exams.
 */
public class PauseSessionResponse {

    /**
     * The id of the {@link Session} that has been paused.
     */
    private UUID sessionId;

    /**
     * The status which the session should be set to once it has been paused.
     */
    private String status;

    /**
     * The date that the session's status was last changed.
     */
    private Instant dateChanged;

    /**
     * The date which the session was closed. Typically null if the session is still open.
     */
    private Instant dateEnded;

    /**
     * A side effect of pausing a {@link Session} is that all the associated Exams must also be paused.  This list will
     * report on the Exams that were affected as a result of pausing this {@link Session}.
     *
     * @return A collection of Exam IDs that belong to the {@link Session} that was paused.
     */
    private List<UUID> examIds;

    // TODO:  DELETE - sample Exam IDs for response to caller.
    public PauseSessionResponse() {
        this.examIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            this.examIds.add(UUID.randomUUID());
        }
    }

    public PauseSessionResponse(Session session) {
        this.sessionId = session.getId();
        this.status = session.getStatus();
        this.dateChanged = session.getDateChanged();
        this.dateEnded = session.getDateEnd();
        this.examIds = new ArrayList<>();
        // TODO:  DELETE - sample Exam IDs for response to caller.
        for (int i = 0; i < 5; i++) {
            this.examIds.add(UUID.randomUUID());
        }
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Instant dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Instant getDateEnded() {
        return dateEnded;
    }

    public void setDateEnded(Instant dateEnded) {
        this.dateEnded = dateEnded;
    }

    public List<UUID> getExamIds() {
        return examIds;
    }

    public void setExamIds(List<UUID> examIds) {
        this.examIds = examIds;
    }
}
