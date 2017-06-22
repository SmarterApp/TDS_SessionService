/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.session;


import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Period;

import java.util.UUID;

/**
 * Represents a session.  A session is information concerning the active session for exams to be taken.
 * <p>
 * NOTE - The legacy code has a session type which is purposefully not included here.  This is done because
 * even though there are types in the legacy system the only ones supported by the online open source application
 * is online.
 * <ul>
 * <li>0 == online</li>
 * <li>1 == paper (not supported)</li>
 * </ul>
 * <p>
 */
public class Session {
    private static final Duration DATE_BEGIN_WINDOW = Period.minutes(5).toStandardDuration();

    private UUID id;
    private String sessionKey;
    private String status;
    private Instant dateBegin;
    private Instant dateEnd;
    private Instant dateChanged;
    private Instant dateVisited;
    private String clientName;
    private Long proctorId;
    private UUID browserKey;
    private String proctorName;
    private String proctorEmail;

    public static class Builder {
        private UUID id;
        private String sessionKey;
        private String status;
        private Instant dateBegin;
        private Instant dateEnd;
        private Instant dateChanged;
        private Instant dateVisited;
        private String clientName;
        private Long proctorId;
        private UUID browserKey;
        private String proctorName;
        private String proctorEmail;

        public Builder withId(UUID newId) {
            id = newId;
            return this;
        }

        public Builder withSessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
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

        public Builder withProctorName(String proctorName) {
            this.proctorName = proctorName;
            return this;
        }

        public Builder withProctorEmail(String proctorEmail) {
            this.proctorEmail = proctorEmail;
            return this;
        }

        public Builder fromSession(Session session) {
            id = session.id;
            sessionKey = session.sessionKey;
            status = session.status;
            dateBegin = session.dateBegin;
            dateEnd = session.dateEnd;
            dateChanged = session.dateChanged;
            dateVisited = session.dateVisited;
            clientName = session.clientName;
            proctorId = session.proctorId;
            browserKey = session.browserKey;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }

    /**
     * Empty constructor for frameworks
     */
    private Session() {
    }

    private Session(Builder builder) {
        id = builder.id;
        sessionKey = builder.sessionKey;
        status = builder.status;
        dateBegin = builder.dateBegin;
        dateEnd = builder.dateEnd;
        dateChanged = builder.dateChanged;
        dateVisited = builder.dateVisited;
        clientName = builder.clientName;
        proctorId = builder.proctorId;
        browserKey = builder.browserKey;
        proctorName = builder.proctorName;
        proctorEmail = builder.proctorEmail;
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
     * This value is displayed on the Proctor's user interface as the Session ID.  Students use this value when
     * logging into the Student UI to take an exam.
     * </p>
     */
    public String getSessionKey() {
        return this.sessionKey;
    }

    /**
     * @return the status of the session. "open" or "closed"
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return The time when the {@link Session} was started.
     * <p>
     * Should handle date and time in UTC
     * </p>
     */
    public Instant getDateBegin() {
        return dateBegin;
    }

    /**
     * @return The time when the {@link Session} was ended.
     * <p>
     * Should handle date and time in UTC
     * </p>
     */
    public Instant getDateEnd() {
        return dateEnd;
    }

    /**
     * @return The date and time when a {@link Session} was changed/updated.
     * <p>
     * Should handle date and time in UTC.  Currently, this field is only updated when the {@link Session}'s status
     * changes.
     * </p>
     */
    public Instant getDateChanged() {
        return dateChanged;
    }

    /**
     * @return The time when the {@link Session} was "visited".
     * <p>
     * Should handle date and time in UTC.  This field is related to evaluating the checkin time.
     * </p>
     */
    public Instant getDateVisited() {
        return dateVisited;
    }

    /**
     * @return The client name for the current tenant
     * <p>
     * Correlates to (at least) the session.externs view, the sessions._externs table and the configs.client_externs
     * table.
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
     * @return The name of the proctor
     */
    public String getProctorName() {
        return proctorName;
    }

    /**
     * @return The email address of the proctor user
     */
    public String getProctorEmail() {
        return proctorEmail;
    }

    /**
     * @return The identifier of the browser information for this {@link Session}.
     * <p>
     * "Browser information" refers to IP address, user-agent etc, from another table.
     * </p>
     */
    public UUID getBrowserKey() {
        return browserKey;
    }

    /**
     * Determine if this {@link Session} is open.  A {@link Session} is open if:
     * <ul>
     * <li>The {@link Session}'s status is "open" (case-insensitive)</li>
     * <li>The end date is before the current UTC time</li>
     * </ul>
     *
     * @return {@code True} if the {@link Session} satisfies the rules above; otherwise {@code False}.
     */
    public Boolean isOpen() {
        final Instant now = Instant.now();
        final String OPEN_STATUS = "open";

        return this.getStatus().toLowerCase().equals(OPEN_STATUS)
            && now.isBefore(this.getDateEnd());
    }

    /**
     * Determine if the session is managed by a Proctor
     * <p>
     * A session can be "proctorless" if the "AnonymousTestee" flag is turned on in the
     * {@code configs.client_systemflags} table.  A user taking a practice test is another example of a "proctorless"
     * session.
     * </p>
     *
     * @return True if the {@code proctorId} is null; otherwise false
     */
    public boolean isProctorless() {
        return this.getProctorId() == null;
    }

    /**
     * Determine if this {@link Session} is a Guest Session.
     * <p>
     * A Guest session is created when a user takes a practice assessment without logging into the Student
     * application.  Also referred to as a "Proctorless session".
     * </p>
     *
     * @return True if the {@link Session}'s session ID is "GUEST Session" (case-insensitive); otherwise false.
     */
    public boolean isGuestSession() {
        final String GUEST_SESSION_ID = "guest session";

        return this.getSessionKey() != null
            && this.getSessionKey().toLowerCase().equals(GUEST_SESSION_ID);
    }
}
