package tds.session;

/**
 * configuration properties for the session system
 */
public class ExternalSessionConfiguration {
    private String clientName;
    private String environment;

    /**
     * @param clientName  client name for the session
     * @param environment environment label for the session
     */
    public ExternalSessionConfiguration(String clientName, String environment) {
        this.clientName = clientName;
        this.environment = environment;
    }

    ExternalSessionConfiguration(){}

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
}
