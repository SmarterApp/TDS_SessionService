package tds.session;

/**
 * configuration properties for the session system
 */
public class ExternalSessionConfiguration {
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
}
