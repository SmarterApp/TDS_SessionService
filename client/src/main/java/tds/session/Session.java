package tds.session;

/**
 * Represents a session.  A session is information concerning the active session for exams to be taken.
 */
public class Session {
    private String status;
    private int type;
    private long id;

    public Session(){}

    /**
     * @param type type of session
     * @param id unique identifier for the session
     */
    public Session(int type, long id) {
        this.type = type;
        this.id = id;
    }

    /**
     * @return the id for the session
     */
    public long getId() {
        return id;
    }

    void setId(long id) {
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
