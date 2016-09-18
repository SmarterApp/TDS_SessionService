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
import tds.session.Extern;
import tds.session.services.ExternService;
import tds.session.web.resources.ExternResource;

@RestController
@RequestMapping("/session/extern")
public class ExternContoller {
    private final ExternService externService;

    public ExternContoller(ExternService externService) {
        this.externService = externService;
    }

    @RequestMapping(value = "/{clientName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ExternResource> getExternByClientName(@PathVariable final String clientName) {
        final Extern extern = externService.getExternByClientName(clientName)
            .orElseThrow((Supplier<RuntimeException>) () -> new NotFoundException("Could not find extern with client name %s", clientName));

        return ResponseEntity.ok(new ExternResource(extern));
    }
}
