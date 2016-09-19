package tds.session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A response indicating the {@link Session} that was paused along with its associated Exams.
 */
public class PauseSessionResponse {
    private Session session;
    private List<UUID> examIds;

    // TODO:  DELETE - sample Exam IDs for response to caller.
    public PauseSessionResponse() {
        this.examIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            this.examIds.add(UUID.randomUUID());
        }
    }

    /**
     * @return The {@link Session} being paused.
     */
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
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

    public void setExamIds(List<UUID> examIds) {
        this.examIds = examIds;
    }
}
