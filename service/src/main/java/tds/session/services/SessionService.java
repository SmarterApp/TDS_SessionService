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

package tds.session.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tds.common.Response;
import tds.session.PauseSessionRequest;
import tds.session.PauseSessionResponse;
import tds.session.Session;
import tds.session.SessionAssessment;

/**
 * Handles interactions with the testing session
 */
public interface SessionService {
    /**
     * Retrieve the {@link tds.session.Session Session} for the given id
     *
     * @param id session id
     * @return Optional containing {@link tds.session.Session Session} otherwise empty
     */
    Optional<Session> findSessionById(final UUID id);

    /**
     * Pause a session, updating it to the specified status.
     * <p>
     * In the context of TDS, "paused" means the session has been stopped and can no longer support students taking
     * exams.  A session that is "stopped" or "closed" (e.g. by a proctor voluntarily ending the session) is
     * considered "paused".
     * </p>
     *
     * @param sessionId the session id of {@link tds.session.Session} should be paused
     * @param request   The proctor id and browser key to identify the requester
     */
    Response<PauseSessionResponse> pause(final UUID sessionId, final PauseSessionRequest request);

    /**
     * Finds the associated {@link tds.session.SessionAssessment}
     *
     * @param sessionId     the session id
     * @param assessmentKey the unique identifier for a particular assessment
     * @return {@link tds.session.SessionAssessment} if found otherwise empty
     */
    Optional<SessionAssessment> findSessionAssessment(final UUID sessionId, final String assessmentKey);

    /**
     * Updates the "date visited" of a {@link tds.session.Session} in order to maintain the session open.
     *
     * @param sessionId The id of the {@link tds.session.Session}
     * @return {@code true} if the session was successfully updated, {@code false} otherwise
     */
    boolean updateDateVisited(final UUID sessionId);

    /**
     * Finds the associated {@link List<tds.session.SessionAssessment>} for a session
     *
     * @param sessionId the id of the session to find the {@link tds.session.SessionAssessment}s for
     * @return the list of all {@link tds.session.SessionAssessment}s for this session
     */
    List<SessionAssessment> findSessionAssessments(final UUID sessionId);

    /**
     * Finds the list of {@link tds.session.Session}s for the specified session ids
     *
     * @param sessionIds The ids of the {@link tds.session.Session}s to find
     * @return A list of sessions for the provided ids
     */
    List<Session> findSessionsByIds(final UUID... sessionIds);
}

