package tds.session.web.resources;

import org.springframework.hateoas.ResourceSupport;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import tds.session.PauseSessionResponse;
import tds.session.web.endpoints.SessionController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS representation of the {@link PauseSessionResponse}.
 */
public class PauseSessionResponseResource extends ResourceSupport {

    private String status;

    private Instant dateChanged;

    private Instant dateEnded;

    List<UUID> examIds;

    public PauseSessionResponseResource(PauseSessionResponse pauseSessionResponse) {;
        this.status = pauseSessionResponse.getStatus();
        this.dateChanged = pauseSessionResponse.getDateChanged();
        this.dateEnded = pauseSessionResponse.getDateEnded();
        this.examIds = pauseSessionResponse.getExamIds();
        this.add(linkTo(
                methodOn(SessionController.class)
                .findSessionById(pauseSessionResponse.getSessionId())
                ).withRel("session"));
    }

    public String getStatus() {
        return status;
    }

    public Instant getDateChanged() {
        return dateChanged;
    }

    public Instant getDateEnded() {
        return dateEnded;
    }

    public List<UUID> getExamIds() {
        return examIds;
    }
}
