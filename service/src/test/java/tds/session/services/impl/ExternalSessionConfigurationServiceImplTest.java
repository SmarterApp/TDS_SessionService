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
        ExternalSessionConfiguration externalSessionConfiguration = new ExternalSessionConfiguration("SBAC", "SIMULATION");
        when(externalSessionConfigurationRepository.findExternalSessionConfigurationByClientName("SBAC")).thenReturn(Optional.of(externalSessionConfiguration));
        Optional<ExternalSessionConfiguration> optional = externalSessionConfigurationService.findExternalSessionConfigurationByClientName("SBAC");
        verify(externalSessionConfigurationRepository).findExternalSessionConfigurationByClientName("SBAC");

        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(externalSessionConfiguration);
    }
}
