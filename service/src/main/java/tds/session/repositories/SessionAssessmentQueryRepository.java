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

import tds.session.SessionAssessment;

/**
 * Handles interaction with the session assessments
 */
public interface SessionAssessmentQueryRepository {
    /**
     * Finds the associated {@link tds.session.SessionAssessment}
     *
     * @param sessionId the session id
     * @return {@link tds.session.SessionAssessment} if found otherwise empty
     */
    Optional<SessionAssessment> findSessionAssessment(final UUID sessionId, final String assessmentKey);

    /**
     * Finds the associated {@link List<tds.session.SessionAssessment>} for a session
     *
     * @param sessionId the id of the session to find the {@link tds.session.SessionAssessment}s for
     * @return the list of all {@link tds.session.SessionAssessment}s for this session
     */
    List<SessionAssessment> findSessionAssessments(final UUID sessionId);
}
