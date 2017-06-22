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
                0, 0, 0);

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
                0, 0, 0);

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
                0, 0, 0);

        assertThat(externalSessionConfiguration.isInSimulationEnvironment()).isFalse();
        assertThat(externalSessionConfiguration.isInDevelopmentEnvironment()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionBecauseClientNameIsNull() {
        new ExternalSessionConfiguration(
            null,
            "Production",
            0,
            0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionBecauseEnvironmentIsNull() {
        new ExternalSessionConfiguration(
            "UNIT_TEST",
            null,
            0,
            0, 0, 0);
    }
}


