package tds.session;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents a session.  A session is information concerning the active session for exams to be taken.
 */
public class Session {
    private UUID id;
    private int type;
    private String status;
    private Timestamp dateBegin;
    private Timestamp dateEnd;
    private Timestamp dateChanged;
    private Timestamp dateVisited;
    private String clientName;
    private Integer proctorId;
    private UUID browserKey;

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

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the type of session
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return The time when the {@link Session} was started.
     * <p>
     *     Should handle date and time in UTC
     * </p>
     */
    public Timestamp getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Timestamp dateBegin) {
        this.dateBegin = dateBegin;
    }

    /**
     * @return The time when the {@link Session} was ended.
     * <p>
     *     Should handle date and time in UTC
     * </p>
     */
    public Timestamp getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Timestamp dateEnd) {
        this.dateEnd = dateEnd;
    }

    /**
     * @return The date and time when a {@link Session} was changed/updated.
     * <p>
     *     Should handle date and time in UTC.  Currently, this field is only updated when the {@link Session}'s status
     *     changes.
     * </p>
     */
    public Timestamp getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Timestamp dateChanged) {
        this.dateChanged = dateChanged;
    }

    /**
     * @return The time when the {@link Session} was "visited".
     * <p>
     *     Should handle date and time in UTC.  This field is related to evaluating the checkin time.
     * </p>
     */
    public Timestamp getDateVisited() {
        return dateVisited;
    }

    public void setDateVisited(Timestamp dateVisited) {
        this.dateVisited = dateVisited;
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

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return The identifier of the Proctor managing this {@link Session}.
     */
    public Integer getProctorId() {
        return proctorId;
    }

    public void setProctorId(Integer proctorId) {
        this.proctorId = proctorId;
    }

    /**
     * @return The identifier of the "browser" (the workstation?) managing this {@link Session}.
     */
    public UUID getBrowserKey() {
        return browserKey;
    }

    public void setBrowserKey(UUID browserKey) {
        this.browserKey = browserKey;
    }

    /**
     * Logic from line 1103 of StudentDLL._ValidateTesteeAccessProc_SP:
     *
     * Date now = _dateUtil.getDateWRetStatus (connection); // returns result of select now(3), found in DataUtilDLL.java
     * // other unrelated things happen...
     * dateBegin = this._commonDll.adjustDateMinutes(dateBegin, Integer.valueOf(-5));
     * if(DbComparator.notEqual(sessionStatus, "open") || DbComparator.lessThan(now, dateBegin) || DbComparator.greaterThan(now, dateEnd)) {
     *  message.set("The session is not available for testing, please check with your test administrator.");
     *  return;
     * }
     * <p>
     *     In the legacy code and performance updates, a SELECT NOW(3); is executed against the database to get the
     *     current date and time.
     * </p>
     * <p>
     *     Calling the {@code toInstant()} method on the begin date and end date will guarantee the date and times are
     *     in UTC (unless a different {@code Clock} is in use).
     * </p>
     *
     * @return {@code True} if the {@link Session}'s status is "open" (case-insensitive) and the current UTC time is
     * between five minutes before the session's start date and the session's end date; otherwise {@code False}.
     */
    public Boolean isOpen() {
        final Instant now = Instant.now();
        final String OPEN_STATUS = "open";

        return this.getStatus().toLowerCase().equals(OPEN_STATUS)
                && now.isAfter(this.getDateBegin().toInstant().minus(5, ChronoUnit.MINUTES))
                && now.isBefore(this.getDateEnd().toInstant());
    }
}
