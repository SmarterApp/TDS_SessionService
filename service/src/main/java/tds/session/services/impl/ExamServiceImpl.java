package tds.session.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import tds.session.configuration.SessionServiceProperties;
import tds.session.services.ExamService;

@Service
class ExamServiceImpl implements ExamService {
    private final RestTemplate restTemplate;
    private final SessionServiceProperties sessionServiceProperties;

    @Autowired
    public ExamServiceImpl(RestTemplate restTemplate, SessionServiceProperties sessionServiceProperties) {
        this.restTemplate = restTemplate;
        this.sessionServiceProperties = sessionServiceProperties;
    }

    @Override
    public void pauseAllExamsInSession(UUID sessionId) {
        UriComponentsBuilder builder =
            UriComponentsBuilder
                .fromHttpUrl(String.format("%s/pause/%s", sessionServiceProperties.getExamUrl(), sessionId));

        restTemplate.put(builder.toUriString(), null);
    }
}
