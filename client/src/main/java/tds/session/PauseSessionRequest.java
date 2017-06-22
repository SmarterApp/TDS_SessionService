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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Data for requesting to pause a {@link tds.session.Session}
 */
public class PauseSessionRequest {
    private UUID browserKey;
    private long proctorId;

    public PauseSessionRequest(@JsonProperty("proctorId") long proctorId, @JsonProperty("browserKey") UUID browserKey) {
        this.proctorId = proctorId;
        this.browserKey = browserKey;
    }

    public UUID getBrowserKey() {
        return browserKey;
    }

    public long getProctorId() {
        return proctorId;
    }
}
