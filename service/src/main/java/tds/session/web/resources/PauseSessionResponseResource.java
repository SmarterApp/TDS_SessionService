package tds.session.web.resources;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import tds.session.PauseSessionResponse;
import tds.session.web.endpoints.SessionController;


import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS representation of the {@link PauseSessionResponse}.
 */
// TODO:  This is returning too much data: should return status, datechanged, dateended, collection of affected examids, and link to session.
public class PauseSessionResponseResource extends ResourceSupport {
    private final PauseSessionResponse pauseSessionResponse;

    public PauseSessionResponseResource(PauseSessionResponse pauseSessionResponse) {
        this.pauseSessionResponse = pauseSessionResponse;
        Resources<UUID> examIdResources = new Resources<>(pauseSessionResponse.getExamIds());
        examIdResources.add(linkTo(
                methodOn(SessionController.class)
                        .getSession(pauseSessionResponse.getSession().getId())
        ).withRel("session"));
        this.add(linkTo(
                methodOn(SessionController.class)
                .getSession(pauseSessionResponse.getSession().getId())
                ).withRel("session"));
    }

    public PauseSessionResponse getPauseSessionResponse() {
        return pauseSessionResponse;
    }
}
