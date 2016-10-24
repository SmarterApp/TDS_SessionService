package tds.session;

import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionTest {
    @Test
    public void shouldBeInstantiated() {
        UUID sessionId = UUID.randomUUID();
        Session session = new Session.Builder()
                .withId(sessionId)
                .withType(0)
                .withStatus("closed")
                .build();

        assertThat(session.getId()).isEqualTo(sessionId);
        assertThat(session.getType()).isEqualTo(0);
        assertThat(session.getStatus()).isEqualTo("closed");
    }

    @Test
    public void shouldBeOpen() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withStatus("open")
                .withDateBegin(Instant.now().minus(20, ChronoUnit.MINUTES))
                .withDateEnd(Instant.now().plus(20, ChronoUnit.MINUTES))
                .build();

        assertThat(session.isOpen()).isTrue();
    }

    @Test
    public void shouldBeOpenAndStatusIsCaseInsensitive() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withStatus("OPEN")
                .withDateBegin(Instant.now().minus(20, ChronoUnit.MINUTES))
                .withDateEnd(Instant.now().plus(20, ChronoUnit.MINUTES))
                .build();

        assertThat(session.isOpen()).isTrue();
    }

    @Test
    public void shouldBeClosedBecauseStatusIsNotOpen() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withStatus("closed")
                .withDateBegin(Instant.now().minus(20, ChronoUnit.MINUTES))
                .withDateEnd(Instant.now().plus(20, ChronoUnit.MINUTES))
                .build();

        assertThat(session.isOpen()).isFalse();
    }

    @Test
    public void shouldBeClosedBecauseSessionHasAlreadyEnded() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withStatus("open")
                .withDateBegin(Instant.now().minus(40, ChronoUnit.MINUTES))
                .withDateEnd(Instant.now().minus(20, ChronoUnit.MINUTES))
                .build();

        assertThat(session.isOpen()).isFalse();
    }

    @Test
    public void shouldBeClosedBecauseSessionHasNotStarted() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withStatus("open")
                .withDateBegin(Instant.now().plus(20, ChronoUnit.MINUTES))
                .withDateEnd(Instant.now().plus(40, ChronoUnit.MINUTES))
                .build();

        assertThat(session.isOpen()).isFalse();
    }

    @Test
    public void shouldCrreateAGuestSession() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withSessionId("guest session")
                .build();

        assertThat(session.isGuestSession()).isTrue();
    }

    @Test
    public void shouldCreateAGuestSessionCaseInsensitive() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withSessionId("GUEST sesSion")
                .build();

        assertThat(session.isGuestSession()).isTrue();
    }

    @Test
    public void shouldCreateARealSessionWithASessionId() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withSessionId("unit test session")
                .build();

        assertThat(session.isGuestSession()).isFalse();
    }

    @Test
    public void shouldCreateARealSessionWhenSessionIdIsNull() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withSessionId(null)
                .build();

        assertThat(session.isGuestSession()).isFalse();
    }

    @Test
    public void shouldReturnTrueForIsProctorlessBecauseProctorIdIsNull() {
        Session session = new Session.Builder()
            .withId(UUID.randomUUID())
            .withProctorId(null)
            .build();

        assertThat(session.isProctorless()).isTrue();
    }

    @Test
    public void shouldReturnFalseForIsProctorlessBecauseProctorIdIsNotNull() {
        Session session = new Session.Builder()
            .withId(UUID.randomUUID())
            .withProctorId(42L)
            .build();

        assertThat(session.isProctorless()).isFalse();
    }
}
