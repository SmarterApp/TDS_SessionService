package tds.session;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    @Test
    public void shouldBeOpen() {
        Session session = new Session();

        session.setId(UUID.randomUUID());
        session.setDateBegin(Timestamp.from(Instant.now().minus(20, ChronoUnit.MINUTES)));
        session.setDateEnd(Timestamp.from(Instant.now().plus(20, ChronoUnit.MINUTES)));
        session.setStatus("open");

        assertThat(session.isOpen()).isTrue();
    }

    @Test
    public void shouldBeOpenAndStatusIsCaseInsensitive() {
        Session session = new Session();

        session.setId(UUID.randomUUID());
        session.setDateBegin(Timestamp.from(Instant.now().minus(20, ChronoUnit.MINUTES)));
        session.setDateEnd(Timestamp.from(Instant.now().plus(20, ChronoUnit.MINUTES)));
        session.setStatus("OPEN");

        assertThat(session.isOpen()).isTrue();
    }

    @Test
    public void shouldBeClosedBecauseStatusIsNotOpen() {
        Session session = new Session();

        session.setId(UUID.randomUUID());
        session.setDateBegin(Timestamp.from(Instant.now().minus(20, ChronoUnit.MINUTES)));
        session.setDateEnd(Timestamp.from(Instant.now().plus(20, ChronoUnit.MINUTES)));
        session.setStatus("closed");

        assertThat(session.isOpen()).isFalse();
    }

    @Test
    public void shouldBeClosedBecauseSessionHasAlreadyEnded() {
        Session session = new Session();

        session.setId(UUID.randomUUID());
        session.setDateBegin(Timestamp.from(Instant.now().minus(40, ChronoUnit.MINUTES)));
        session.setDateEnd(Timestamp.from(Instant.now().minus(20, ChronoUnit.MINUTES)));
        session.setStatus("open");

        assertThat(session.isOpen()).isFalse();
    }

    @Test
    public void shouldBeClosedBecauseSessionHasNotStarted() {
        Session session = new Session();

        session.setId(UUID.randomUUID());
        session.setDateBegin(Timestamp.from(Instant.now().plus(20, ChronoUnit.MINUTES)));
        session.setDateEnd(Timestamp.from(Instant.now().plus(40, ChronoUnit.MINUTES)));
        session.setStatus("open");

        assertThat(session.isOpen()).isFalse();
    }
}
