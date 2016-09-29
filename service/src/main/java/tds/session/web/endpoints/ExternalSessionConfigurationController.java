package tds.session.web.endpoints;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

import tds.common.web.exceptions.NotFoundException;
import tds.session.ExternalSessionConfiguration;
import tds.session.services.ExternalSessionConfigurationService;

@RestController
@RequestMapping("/sessions/external-config")
public class ExternalSessionConfigurationController {
    private final ExternalSessionConfigurationService externalSessionConfigurationService;

    public ExternalSessionConfigurationController(ExternalSessionConfigurationService externalSessionConfigurationService) {
        this.externalSessionConfigurationService = externalSessionConfigurationService;
    }

    @RequestMapping(value = "/{clientName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ExternalSessionConfiguration> findExternalSessionConfigurationByClientName(@PathVariable final String clientName) {
        final ExternalSessionConfiguration externalSessionConfiguration = externalSessionConfigurationService.findExternalSessionConfigurationByClientName(clientName)
            .orElseThrow((Supplier<RuntimeException>) () -> new NotFoundException("Could not find extern with client name %s", clientName));

        return ResponseEntity.ok(externalSessionConfiguration);
    }
}
