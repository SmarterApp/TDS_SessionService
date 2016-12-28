package tds.session.services;

import java.util.Optional;

import tds.session.ExternalSessionConfiguration;

public interface ExternalSessionConfigurationService {
    /**
     * Fetches the external session configuration by given client name
     *
     * @param clientName client name for extern
     * @return optional containing {@link tds.session.ExternalSessionConfiguration Extern} otherwise empty Optional
     */
    Optional<ExternalSessionConfiguration> findExternalSessionConfigurationByClientName(String clientName);
}
