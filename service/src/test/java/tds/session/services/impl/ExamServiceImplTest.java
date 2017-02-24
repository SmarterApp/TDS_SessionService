package tds.session.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import tds.session.configuration.SessionServiceProperties;
import tds.session.services.ExamService;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExamServiceImplTest {
    private static final String BASE_URL = "http://localhost:8080";

    private ExamService examService;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        SessionServiceProperties sessionServiceProperties = new SessionServiceProperties();
        sessionServiceProperties.setExamUrl(BASE_URL);
        examService = new ExamServiceImpl(restTemplate, sessionServiceProperties);
    }

    @Test
    public void shouldPauseAllExamsInASession() {
        UUID sessionId = UUID.randomUUID();
        String url = String.format("%s/%s/pause/%s", BASE_URL, ExamServiceImpl.APP_ROOT_CONTEXT, sessionId);

        examService.pauseAllExamsInSession(sessionId);

        verify(restTemplate).put(url, null);
    }
}
