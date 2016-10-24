package tds.session;

/**
 * configuration properties for the session system
 */
public class ExternalSessionConfiguration {
    final String SIMULATION_ENVIRONMENT = "simulation";
    final String DEVELOPMENT_ENVIRONMENT = "development";

    private String clientName;
    private String environment;
    private int shiftWindowStart;
    private int shiftWindowEnd;

    /**
     * @param clientName       client name for the session
     * @param environment      environment label for the session
     * @param shiftWindowStart number of days to shift the window start
     * @param shiftWindowEnd   number of days to shift the window end
     */
    public ExternalSessionConfiguration(String clientName,
                                        String environment,
                                        int shiftWindowStart,
                                        int shiftWindowEnd) {
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
