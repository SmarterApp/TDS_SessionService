package tds.session.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import tds.common.cache.CacheType;
import tds.session.ExternalSessionConfiguration;
import tds.session.repositories.ExternalSessionConfigurationRepository;
import tds.session.services.ExternalSessionConfigurationService;

@Service
public class ExternalSessionConfigurationServiceImpl implements ExternalSessionConfigurationService {
    private final ExternalSessionConfigurationRepository repository;

    @Autowired
    public ExternalSessionConfigurationServiceImpl(ExternalSessionConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(CacheType.MEDIUM_TERM)
    public Optional<ExternalSessionConfiguration> findExternalSessionConfigurationByClientName(String clientName) {
        return repository.findExternalSessionConfigurationByClientName(clientName);
    }
}
