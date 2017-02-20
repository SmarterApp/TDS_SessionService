package tds.session.repositories;

import java.util.Optional;

import tds.session.ExternalSessionConfiguration;

/**
 * Repository handling data access relating to {@link tds.session.ExternalSessionConfiguration}
 */
public interface ExternalSessionConfigurationRepository {
    /**
     * Fetches the external session configuration by given client name
     *
     * @param clientName client name for external session configuration
     * @return optional containing {@link tds.session.ExternalSessionConfiguration Extern} otherwise empty Optional
     */
    Optional<ExternalSessionConfiguration> findExternalSessionConfigurationByClientName(final String clientName);
}
