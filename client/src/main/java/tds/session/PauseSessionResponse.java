package tds.session;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A response indicating the {@link Session} that was paused along with its associated Exams.
 */
public class PauseSessionResponse {
    private UUID sessionId;
    private String status;
    private Instant dateChanged;
    private Instant dateEnded;
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

    /**
     * @return The id of the {@link Session} that has been paused.
     */
    public UUID getSessionId() {
        return sessionId;
    }

    /**
     * @return The status which the session should be set to once it has been paused.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return The date that the session's status was last changed.
     */
    public Instant getDateChanged() {
        return dateChanged;
    }

    /**
     * @return The date which the session was closed. Typically null if the session is still open.
     */
    public Instant getDateEnded() {
        return dateEnded;
    }

    /**
     * A side effect of pausing a {@link Session} is that all the associated Exams must also be paused.  This list will
     * report on the Exams that were affected as a result of pausing this {@link Session}.
     *
     * @return A collection of Exam IDs that belong to the {@link Session} that was paused.
     */
    public List<UUID> getExamIds() {
        return examIds;
    }
}
