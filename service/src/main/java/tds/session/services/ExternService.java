package tds.session.services;

import java.util.Optional;

import tds.session.Extern;

public interface ExternService {
    /**
     * Fetches the extern by given client name
     * @param clientName client name for extern
     * @return optional containing {@link tds.session.Extern Extern} otherwise empty Optional
     */
    Optional<Extern> getExternByClientName(String clientName);
}