package tds.session;

/**
 * configuration properties for the session system
 */
public class Extern {
    private String clientName;
    private String environment;

    public static class Builder {
        private String clientName;
        private String environment;

        public Builder withClientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder withEnvironment(String environment) {
            this.environment = environment;
            return this;
        }

        public Extern build() {
            Extern extern = new Extern();
            extern.clientName = this.clientName;
            extern.environment = this.environment;
            return extern;
        }
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
