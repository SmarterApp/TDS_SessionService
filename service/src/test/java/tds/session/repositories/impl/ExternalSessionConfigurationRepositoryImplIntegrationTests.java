package tds.session.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import tds.session.ExternalSessionConfiguration;
import tds.session.repositories.ExternalSessionConfigurationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExternalSessionConfigurationRepositoryImplIntegrationTests {
    @Autowired
    private ExternalSessionConfigurationRepository externalSessionConfigurationRepository;

    @Test
    public void shouldFindExternalSessionConfigurationByClientName() {
        Optional<ExternalSessionConfiguration> maybeExternalSessionConfiguration = externalSessionConfigurationRepository.findExternalSessionConfigurationByClientName("SBAC");
        assertThat(maybeExternalSessionConfiguration).isPresent();
        assertThat(maybeExternalSessionConfiguration.get().getClientName()).isEqualTo("SBAC");
        assertThat(maybeExternalSessionConfiguration.get().getEnvironment()).isEqualTo("Development");
    }

    @Test
    public void shouldHandleWhenExternCannotBeFoundByClientName() {
        Optional<ExternalSessionConfiguration> maybeExternalSessionConfiguration = externalSessionConfigurationRepository.findExternalSessionConfigurationByClientName("FAKE");
        assertThat(maybeExternalSessionConfiguration).isNotPresent();
    }
}
