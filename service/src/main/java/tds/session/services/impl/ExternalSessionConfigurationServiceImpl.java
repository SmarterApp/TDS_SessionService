/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

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
    public ExternalSessionConfigurationServiceImpl(final ExternalSessionConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
    public Optional<ExternalSessionConfiguration> findExternalSessionConfigurationByClientName(final String clientName) {
        return repository.findExternalSessionConfigurationByClientName(clientName);
    }
}
