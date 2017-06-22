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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import tds.common.web.exceptions.NotFoundException;
import tds.session.ExternalSessionConfiguration;
import tds.session.services.ExternalSessionConfigurationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExternalSessionConfigurationControllerTest {
    private ExternalSessionConfigurationController controller;
    private ExternalSessionConfigurationService externalSessionConfigurationService;

    @Before
    public void setUp() {
        HttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        externalSessionConfigurationService = mock(ExternalSessionConfigurationService.class);
        controller = new ExternalSessionConfigurationController(externalSessionConfigurationService);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldFindExternalSessionConfigurationByClientName() {
        ExternalSessionConfiguration externalSessionConfiguration = new ExternalSessionConfiguration("SBAC", "Development", 0, 0, 10, 11);
        when(externalSessionConfigurationService.findExternalSessionConfigurationByClientName("SBAC")).thenReturn(Optional.of(externalSessionConfiguration));

        ResponseEntity<ExternalSessionConfiguration> response = controller.findExternalSessionConfigurationByClientName("SBAC");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(externalSessionConfiguration);
    }

    @Test(expected = NotFoundException.class)
    public void shouldHandleNotFoundExternalSessionConfigurationByClientName() {
        when(externalSessionConfigurationService.findExternalSessionConfigurationByClientName("SBAC")).thenReturn(Optional.empty());
        controller.findExternalSessionConfigurationByClientName("SBAC");
    }

}
