package tds.session.services.impl;

import org.springframework.stereotype.Service;

import java.util.Optional;

import tds.session.Extern;
import tds.session.repositories.ExternRepository;
import tds.session.services.ExternService;

@Service
public class ExternServiceImpl implements ExternService{
    private final ExternRepository repository;

    public ExternServiceImpl(ExternRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Extern> getExternByClientName(String clientName) {
        return repository.getExternByClientName(clientName);
    }
}
