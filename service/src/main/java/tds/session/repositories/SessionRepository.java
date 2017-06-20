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

package tds.session.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tds.session.Session;

/**
 * Handles the data access for {@link tds.session.Session Session}
 */
public interface SessionRepository {
    /**
     * Finds the list of {@link tds.session.Session}s for the specified session ids
     *
     * @param ids The ids of the {@link tds.session.Session}s to find
     * @return A list of sessions for the provided ids
     */
    List<Session> findSessionsByIds(final UUID... ids);

    /**
     * Pause an existing {@link Session}, updating the {@link Session}'s status to indicate it is no longer "open".
     * <p>
     * The database design for microservices is to never execute an UPDATE against existing records.  Any changes
     * to the state of an object should be recorded as a new record.  For example, when a session is paused, its
     * state is changed.  Rather than updating the existing record in the session.session table, a new record should
     * be inserted into a newStatus table that records the change.
     * </p>
     *
     * @param sessionId The id of the {@link Session} to pause
     */
    void pause(final UUID sessionId);

    /**
     * Updates the "datevisited" column of the session table to prevent the session from being closed from a timeout due
     * to proctor inactivity.
     *
     * @param sessionId The id of the {@link tds.session.Session} to extend
     */
    void updateDateVisited(UUID sessionId);
}
