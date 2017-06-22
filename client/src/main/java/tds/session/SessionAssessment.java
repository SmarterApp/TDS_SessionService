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

import java.util.UUID;

/**
 * Assessment information for the session
 */
public class SessionAssessment {
    private UUID sessionId;
    private String assessmentId;
    private String assessmentKey;

    /**
     * @param sessionId     session id
     * @param assessmentId  the overall assessment id
     * @param assessmentKey the specific unique assessment key for the session assessment
     */
    public SessionAssessment(UUID sessionId, String assessmentId, String assessmentKey) {
        this.sessionId = sessionId;
        this.assessmentId = assessmentId;
        this.assessmentKey = assessmentKey;
    }

    /**
     * Private constructor for frameworks
     */
    private SessionAssessment() {
    }

    /**
     * @return session id
     */
    public UUID getSessionId() {
        return sessionId;
    }

    /**
     * @return the assessment id
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    /**
     * @return unique key for a specific assessment
     */
    public String getAssessmentKey() {
        return assessmentKey;
    }
}
