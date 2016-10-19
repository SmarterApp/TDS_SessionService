package tds.session.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import tds.session.ExternalSessionConfiguration;
import tds.session.services.ExternalSessionConfigurationService;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExternalSessionConfigurationServiceIntegrationTests {
    @Autowired
    private ExternalSessionConfigurationService externalSessionConfigurationService;

    @Test
    public void shouldFindExternalSessionConfigurationByClientName() {
        ExternalSessionConfiguration externalSessionConfiguration = externalSessionConfigurationService.findExternalSessionConfigurationByClientName("SBAC").get();

        assertThat(externalSessionConfiguration.getClientName()).isEqualTo("SBAC");
        assertThat(externalSessionConfiguration.getEnvironment()).isEqualTo("Development");
        assertThat(externalSessionConfiguration.getShiftWindowEnd()).isEqualTo(3);
        assertThat(externalSessionConfiguration.getShiftWindowStart()).isEqualTo(5);
    }

    @Test
    public void shouldHandleWhenExternCannotBeFoundByClientName() {
        Optional<ExternalSessionConfiguration> maybeExternalSessionConfiguration = externalSessionConfigurationService.findExternalSessionConfigurationByClientName("FAKE");
        assertThat(maybeExternalSessionConfiguration).isNotPresent();
    }
}
