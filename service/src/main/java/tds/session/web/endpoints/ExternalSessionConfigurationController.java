/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.session.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
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
class ExternalSessionConfigurationController {
    private final ExternalSessionConfigurationService externalSessionConfigurationService;

    @Autowired
    public ExternalSessionConfigurationController(ExternalSessionConfigurationService externalSessionConfigurationService) {
        this.externalSessionConfigurationService = externalSessionConfigurationService;
    }

    @RequestMapping(value = "/{clientName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<ExternalSessionConfiguration> findExternalSessionConfigurationByClientName(@PathVariable final String clientName) {
        final ExternalSessionConfiguration externalSessionConfiguration = externalSessionConfigurationService.findExternalSessionConfigurationByClientName(clientName)
            .orElseThrow((Supplier<RuntimeException>) () -> new NotFoundException("Could not find extern with client name %s", clientName));

        return ResponseEntity.ok(externalSessionConfiguration);
    }
}
