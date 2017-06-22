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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Optional;

import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;
import tds.session.ExternalSessionConfiguration;
import tds.session.services.ExternalSessionConfigurationService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ExternalSessionConfigurationController.class)
@Import({ExceptionAdvice.class, SecurityConfiguration.class})
public class ExternalSessionConfigurationControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private ExternalSessionConfigurationService service;

    @Test
    public void shouldReturnExternalSessionConfiguration() throws Exception {
        ExternalSessionConfiguration externalSessionConfiguration = new ExternalSessionConfiguration("SBAC", "development", 3, 5, 10, 11);
        when(service.findExternalSessionConfigurationByClientName("SBAC")).thenReturn(Optional.of(externalSessionConfiguration));

        http.perform(get(new URI("/sessions/external-config/SBAC"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("clientName", is("SBAC")))
            .andExpect(jsonPath("environment", is("development")))
            .andExpect(jsonPath("shiftWindowStart", is(3)))
            .andExpect(jsonPath("shiftWindowEnd", is(5)))
            .andExpect(jsonPath("shiftFormStart", is(10)))
            .andExpect(jsonPath("shiftFormEnd", is(11)));
    }

    @Test
    public void shouldReturnNotFoundWhenExternalSessionConfigurationCannotBeFoundByClientName() throws Exception {
        when(service.findExternalSessionConfigurationByClientName("SBAC")).thenReturn(Optional.empty());
        http.perform(get(new URI("/sessions/external-config/SBAC"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}

