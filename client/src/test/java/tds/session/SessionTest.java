package tds.session;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionTest {
    @Test
    public void shouldBeInstantiated() {
        Session session = new Session();
        UUID sessionId = UUID.randomUUID();
        session.setId(sessionId);
        session.setType(0);
        session.setStatus("closed");

        assertThat(session.getId()).isEqualTo(sessionId);
        assertThat(session.getType()).isEqualTo(0);
        assertThat(session.getStatus()).isEqualTo("closed");
    }
}
