package tds.session.web.resources;

import org.springframework.hateoas.ResourceSupport;

import tds.session.ExternalSessionConfiguration;
import tds.session.web.endpoints.ExternalSessionConfigurationController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ExternalSessionConfigurationResource extends ResourceSupport {
    private final ExternalSessionConfiguration externalSessionConfiguration;

    public ExternalSessionConfigurationResource(ExternalSessionConfiguration externalSessionConfiguration) {
        this.externalSessionConfiguration = externalSessionConfiguration;
        this.add(linkTo(
            methodOn(ExternalSessionConfigurationController.class)
                .findExternalSessionConfigurationByClientName(externalSessionConfiguration.getClientName()))
            .withSelfRel());
    }

    public ExternalSessionConfiguration getExternalSessionConfiguration() {
        return externalSessionConfiguration;
    }
}
