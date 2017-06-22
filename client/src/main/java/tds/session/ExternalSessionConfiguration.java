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

/**
 * configuration properties for the session system
 */
public class ExternalSessionConfiguration {
    public static final String SIMULATION_ENVIRONMENT = "simulation";
    public static final String DEVELOPMENT_ENVIRONMENT = "development";

    private String clientName;
    private String environment;
    private int shiftWindowStart;
    private int shiftWindowEnd;
    private int shiftFormStart;
    private int shiftFormEnd;

    /**
     * @param clientName       client name for the session
     * @param environment      environment label for the session
     * @param shiftWindowStart number of days to shift the window start
     * @param shiftWindowEnd   number of days to shift the window end
     */
    public ExternalSessionConfiguration(String clientName,
                                        String environment,
                                        int shiftWindowStart,
                                        int shiftWindowEnd,
                                        int shiftFormStart,
                                        int shiftFormEnd) {
        if (clientName == null) {
            throw new IllegalArgumentException("clientname cannot be null");
        }

        if (environment == null) {
            throw new IllegalArgumentException("environment cannot be null");
        }

        this.clientName = clientName;
        this.environment = environment;
        this.shiftWindowStart = shiftWindowStart;
        this.shiftWindowEnd = shiftWindowEnd;
        this.shiftFormStart = shiftFormStart;
        this.shiftFormEnd = shiftFormEnd;
    }

    /**
     * Empty constructor for frameworks
     */
    private ExternalSessionConfiguration() {
    }

    /**
     * @return client name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return environment type
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * @return number of days to shift the window start
     */
    public int getShiftWindowStart() {
        return shiftWindowStart;
    }

    /**
     * @return number of days to shift the window end
     */
    public int getShiftWindowEnd() {
        return shiftWindowEnd;
    }

    /**
     * @return the number of days to shift the form start
     */
    public int getShiftFormStart() {
        return shiftFormStart;
    }

    /**
     * @return the number of days to shift the form end
     */
    public int getShiftFormEnd() {
        return shiftFormEnd;
    }

    /**
     * Determine if this {@link ExternalSessionConfiguration} is configured for the Simulation environment.
     *
     * @return True if the environment is set to "simulation" (case-insensitive); otherwise false.
     */
    public boolean isInSimulationEnvironment() {
        return this.getEnvironment().equalsIgnoreCase(SIMULATION_ENVIRONMENT);
    }

    /**
     * Determine if this {@link ExternalSessionConfiguration} is configured for the Development environment.
     *
     * @return True if the environment is set to "development" (case-insensitive); otherwise false.
     */
    public boolean isInDevelopmentEnvironment() {
        return this.getEnvironment().equalsIgnoreCase(DEVELOPMENT_ENVIRONMENT);
    }
}
