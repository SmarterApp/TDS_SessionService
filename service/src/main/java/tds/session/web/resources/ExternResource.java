package tds.session.web.resources;

import org.springframework.hateoas.ResourceSupport;

import tds.session.Extern;
import tds.session.web.endpoints.ExternContoller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ExternResource extends ResourceSupport {
    private final Extern extern;

    public ExternResource(Extern extern) {
        this.extern = extern;
        this.add(linkTo(
            methodOn(ExternContoller.class)
                .getExternByClientName(extern.getClientName()))
            .withSelfRel());
    }

    public Extern getExtern() {
        return extern;
    }
}
