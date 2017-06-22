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
