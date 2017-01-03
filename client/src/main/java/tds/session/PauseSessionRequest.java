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
