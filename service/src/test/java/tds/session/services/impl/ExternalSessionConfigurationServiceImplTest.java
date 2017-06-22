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

package tds.session.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import tds.session.ExternalSessionConfiguration;
import tds.session.repositories.ExternalSessionConfigurationRepository;
import tds.session.services.ExternalSessionConfigurationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExternalSessionConfigurationServiceImplTest {
    private ExternalSessionConfigurationRepository externalSessionConfigurationRepository;
    private ExternalSessionConfigurationService externalSessionConfigurationService;

    @Before
    public void setUp() {
        externalSessionConfigurationRepository = mock(ExternalSessionConfigurationRepository.class);
        externalSessionConfigurationService = new ExternalSessionConfigurationServiceImpl(externalSessionConfigurationRepository);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldFindExternalSessionConfigurationByClientName() {
        ExternalSessionConfiguration externalSessionConfiguration = new ExternalSessionConfiguration("SBAC", "SIMULATION", 0, 0, 10, 11);
        when(externalSessionConfigurationRepository.findExternalSessionConfigurationByClientName("SBAC")).thenReturn(Optional.of(externalSessionConfiguration));
        Optional<ExternalSessionConfiguration> optional = externalSessionConfigurationService.findExternalSessionConfigurationByClientName("SBAC");
        verify(externalSessionConfigurationRepository).findExternalSessionConfigurationByClientName("SBAC");

        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(externalSessionConfiguration);
    }
}
