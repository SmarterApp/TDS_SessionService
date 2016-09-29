package tds.session;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents a session.  A session is information concerning the active session for exams to be taken.
 */
public class Session {
    private UUID id;
    private String sessionId;
    private int type;
    private String status;
    private Instant dateBegin;
    private Instant dateEnd;
    private Instant dateChanged;
    private Instant dateVisited;
    private String clientName;
    private Long proctorId;
    private UUID browserKey;

    public static class Builder {
        private UUID id;
        private String sessionId;
        private int type;
        private String status;
        private Instant dateBegin;
        private Instant dateEnd;
        private Instant dateChanged;
        private Instant dateVisited;
        private String clientName;
        private Long proctorId;
        private UUID browserKey;

        public Builder withId(UUID newId) {
            id = newId;
            return this;
        }

        public Builder withSessionId(String newSessionId) {
            sessionId = newSessionId;
            return this;
        }

        public Builder withType(int newType) {
            type = newType;
            return this;
        }

        public Builder withStatus(String newStatus) {
            status = newStatus;
            return this;
        }

        public Builder withDateBegin(Instant newDateBegin) {
            dateBegin = newDateBegin;
            return this;
        }

        public Builder withDateEnd(Instant newDateEnd) {
            dateEnd = newDateEnd;
            return this;
        }

        public Builder withDateChanged(Instant newDateChanged) {
            dateChanged = newDateChanged;
            return this;
        }

        public Builder withDateVisited(Instant newDateVisited) {
            dateVisited = newDateVisited;
            return this;
        }

        public Builder withClientName(String newClientName) {
            clientName = newClientName;
            return this;
        }

        public Builder withProctorId(Long newProctorId) {
            proctorId = newProctorId;
            return this;
        }

        public Builder withBrowserKey(UUID newBrowserKey) {
            browserKey = newBrowserKey;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }

    /**
     * Empty constructor for frameworks
     */
    private Session() {}

    private Session(Builder builder) {
        id = builder.id;
        sessionId = builder.sessionId;
        type = builder.type;
        status = builder.status;
        dateBegin = builder.dateBegin;
        dateEnd = builder.dateEnd;
        dateChanged = builder.dateChanged;
        dateVisited = builder.dateVisited;
        clientName = builder.clientName;
        proctorId = builder.proctorId;
        browserKey = builder.browserKey;
    }

    /**
     * @return the id for the session
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return The public session identifier for this {@link Session}.
     * <p>
     *     This value is displayed on the Proctor's user interface as the Session ID.  Students use this value when
     *     logging into the Student UI to take an exam.
     * </p>
     */
    public String getSessionId() {
        return this.sessionId;
    }

    /**
     * @return the status of the session. "open" or "closed"
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the type of session
     */
    public int getType() {
        return type;
    }

    /**
     * @return The time when the {@link Session} was started.
     * <p>
     *     Should handle date and time in UTC
     * </p>
     */
    public Instant getDateBegin() {
        return dateBegin;
    }

    /**
     * @return The time when the {@link Session} was ended.
     * <p>
     *     Should handle date and time in UTC
     * </p>
     */
    public Instant getDateEnd() {
        return dateEnd;
    }

    /**
     * @return The date and time when a {@link Session} was changed/updated.
     * <p>
     *     Should handle date and time in UTC.  Currently, this field is only updated when the {@link Session}'s status
     *     changes.
     * </p>
     */
    public Instant getDateChanged() {
        return dateChanged;
    }

    /**
     * @return The time when the {@link Session} was "visited".
     * <p>
     *     Should handle date and time in UTC.  This field is related to evaluating the checkin time.
     * </p>
     */
    public Instant getDateVisited() {
        return dateVisited;
    }

    /**
     * @return The client name for the current tenant
     * <p>
     *     Correlates to (at least) the session.externs view, the sessions._externs table and the configs.client_externs
     *     table.
     * </p>
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return The identifier of the Proctor managing this {@link Session}.
     */
    public Long getProctorId() {
        return proctorId;
    }

    /**
     * @return The identifier of the browser information for this {@link Session}.
     * <p>
     *     "Browser information" refers to IP address, user-agent etc, from another table.
     * </p>
     */
    public UUID getBrowserKey() {
        return browserKey;
    }

    /**
     * Determine if this {@link Session} is open.  A {@link Session} is open if:
     * <ul>
     *     <li>The {@link Session}'s status is "open" (case-insensitive)</li>
     *     <li>The current UTC time is after five minutes before this {@link Session}'s begin date</li>
     *     <li>The end date is before the current UTC time</li>
     * </ul>
     *
     * @return {@code True} if the {@link Session} satisfies the rules above; otherwise {@code False}.
     */
    public Boolean isOpen() {
        final Instant now = Instant.now();
        final String OPEN_STATUS = "open";

        return this.getStatus().toLowerCase().equals(OPEN_STATUS)
                && now.isAfter(this.getDateBegin().minus(5, ChronoUnit.MINUTES))
                && now.isBefore(this.getDateEnd());
    }

    /**
     * Determine if this {@link Session} is a Guest Session.
     * <p>
     *     A Guest session is created when a user takes a practice assessment without logging into the Student
     *     application.  Also referred to as a "Proctorless session".
     * </p>
     *
     * @return True if the {@link Session}'s session ID is "GUEST Session" (case-insensitive); otherwise false.
     */
    public boolean isGuestSession() {
        final String GUEST_SESSION_ID = "guest session";

        return this.getSessionId() != null
                && this.getSessionId().toLowerCase().equals(GUEST_SESSION_ID);
    }
}
