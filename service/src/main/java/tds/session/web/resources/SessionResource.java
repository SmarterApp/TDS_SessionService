package tds.session.web.resources;

import org.springframework.hateoas.ResourceSupport;

import tds.session.Session;
import tds.session.web.endpoints.SessionController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class SessionResource extends ResourceSupport{
    private final Session session;

    public SessionResource(Session session) {
        this.session = session;
        this.add(linkTo(
                methodOn(SessionController.class)
                        .getSession(session.getId()))
                .withSelfRel());
    }

    public Session getSession() {
        return session;
    }
}
