package tds.session;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionTest {
    private static final Duration STANDARD_DURATION = Period.minutes(20).toStandardDuration();

    @Test
    public void shouldBeInstantiated() {
        UUID sessionId = UUID.randomUUID();
        Session session = new Session.Builder()
                .withId(sessionId)
                .withStatus("closed")
                .build();

        assertThat(session.getId()).isEqualTo(sessionId);
        assertThat(session.getStatus()).isEqualTo("closed");
    }

    @Test
    public void shouldBeOpen() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withStatus("open")
                .withDateBegin(Instant.now().minus(STANDARD_DURATION))
                .withDateEnd(Instant.now().plus(STANDARD_DURATION))
                .build();

        assertThat(session.isOpen()).isTrue();
    }

    @Test
    public void shouldBeOpenAndStatusIsCaseInsensitive() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withStatus("OPEN")
                .withDateBegin(Instant.now().minus(STANDARD_DURATION))
                .withDateEnd(Instant.now().plus(STANDARD_DURATION))
                .build();

        assertThat(session.isOpen()).isTrue();
    }

    @Test
    public void shouldBeClosedBecauseStatusIsNotOpen() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withStatus("closed")
                .withDateBegin(Instant.now().minus(STANDARD_DURATION))
                .withDateEnd(Instant.now().plus(STANDARD_DURATION))
                .build();

        assertThat(session.isOpen()).isFalse();
    }

    @Test
    public void shouldBeClosedBecauseSessionHasAlreadyEnded() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withStatus("open")
                .withDateBegin(Instant.now().minus(Period.minutes(40).toStandardDuration()))
                .withDateEnd(Instant.now().minus(STANDARD_DURATION))
                .build();

        assertThat(session.isOpen()).isFalse();
    }

    @Test
    public void shouldCrreateAGuestSession() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withSessionKey("guest session")
                .build();

        assertThat(session.isGuestSession()).isTrue();
    }

    @Test
    public void shouldCreateAGuestSessionCaseInsensitive() {
        Session session = new Session.Builder()
            .withId(UUID.randomUUID())
            .withSessionKey("GUEST sesSion")
            .build();

        assertThat(session.isGuestSession()).isTrue();
    }

    @Test
    public void shouldCreateARealSessionWithASessionId() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withSessionKey("unit test session")
                .build();

        assertThat(session.isGuestSession()).isFalse();
    }

    @Test
    public void shouldCreateARealSessionWhenSessionIdIsNull() {
        Session session = new Session.Builder()
                .withId(UUID.randomUUID())
                .withSessionKey(null)
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
