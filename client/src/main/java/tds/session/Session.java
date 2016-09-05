package tds.session;

import java.util.UUID;

/**
 * Represents a session.  A session is information concerning the active session for exams to be taken.
 */
public class Session {
    private String status;
    private int type;
    private UUID id;

    public Session(){}

    /**
     * @param id unique identifier for the session
     * @param type type of session
     */
    public Session(UUID id, int type) {
        this.type = type;
        this.id = id;
    }

    /**
     * @return the id for the session
     */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return the status of the session. "open" or "closed"
     */
    public String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the type of session
     */
    public int getType() {
        return type;
    }

    void setType(int type) {
        this.type = type;
    }
}
