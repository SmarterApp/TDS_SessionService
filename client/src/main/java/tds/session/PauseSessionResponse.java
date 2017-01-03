package tds.session;

import org.joda.time.Instant;

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

    public PauseSessionResponse(Session session) {
        this.sessionId = session.getId();
        this.status = session.getStatus();
        this.dateChanged = session.getDateChanged();
        this.dateEnded = session.getDateEnd();
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
}
