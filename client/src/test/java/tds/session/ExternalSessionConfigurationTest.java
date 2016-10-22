package tds.session;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExternalSessionConfigurationTest {
    @Test
    public void shouldReturnTrueForIsInSimulationEnvironment() {
        ExternalSessionConfiguration externalSessionConfiguration =
            new ExternalSessionConfiguration(
                "UNIT_TEST",
                "Simulation",
                0,
                0);

        assertThat(externalSessionConfiguration.isInSimulationEnvironment()).isTrue();
        assertThat(externalSessionConfiguration.isInDevelopmentEnvironment()).isFalse();
    }

    @Test
    public void shouldReturnTrueForIsInDevelopmentEnvironment() {
        ExternalSessionConfiguration externalSessionConfiguration =
            new ExternalSessionConfiguration(
                "UNIT_TEST",
                "Development",
                0,
                0);

        assertThat(externalSessionConfiguration.isInDevelopmentEnvironment()).isTrue();
        assertThat(externalSessionConfiguration.isInSimulationEnvironment()).isFalse();
    }

    @Test
    public void shouldReturnFalseForIsInSimulationEnvironmentAndIsInDevelopmentEnvironment() {
        ExternalSessionConfiguration externalSessionConfiguration =
            new ExternalSessionConfiguration(
                "UNIT_TEST",
                "Production",
                0,
                0);

        assertThat(externalSessionConfiguration.isInSimulationEnvironment()).isFalse();
        assertThat(externalSessionConfiguration.isInDevelopmentEnvironment()).isFalse();
    }
}


