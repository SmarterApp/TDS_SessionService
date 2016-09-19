package tds.session;

/**
 * configuration properties for the session system
 */
public class Extern {
    private final String clientName;
    private final String environment;

    /**
     * @param clientName  client name for the session
     * @param environment environment label for the session
     */
    public Extern(String clientName, String environment) {
        this.clientName = clientName;
        this.environment = environment;
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
}
