package tds.session.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import tds.common.web.exceptions.NotFoundException;
import tds.session.ExternalSessionConfiguration;
import tds.session.services.ExternalSessionConfigurationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExternalSessionConfigurationControllerTest {
    private ExternalSessionConfigurationController controller;
    private ExternalSessionConfigurationService externalSessionConfigurationService;

    @Before
    public void setUp() {
        HttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        externalSessionConfigurationService = mock(ExternalSessionConfigurationService.class);
        controller = new ExternalSessionConfigurationController(externalSessionConfigurationService);
    }

    @After
    public void tearDown() {}

    @Test
    public void shouldFindExternalSessionConfigurationByClientName() {
        ExternalSessionConfiguration externalSessionConfiguration = new ExternalSessionConfiguration("SBAC", "Development", 0, 0);
        when(externalSessionConfigurationService.findExternalSessionConfigurationByClientName("SBAC")).thenReturn(Optional.of(externalSessionConfiguration));

        ResponseEntity<ExternalSessionConfiguration> response = controller.findExternalSessionConfigurationByClientName("SBAC");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(externalSessionConfiguration);
    }

    @Test(expected = NotFoundException.class)
    public void shouldHandleNotFoundExternalSessionConfigurationByClientName() {
        when(externalSessionConfigurationService.findExternalSessionConfigurationByClientName("SBAC")).thenReturn(Optional.empty());
        controller.findExternalSessionConfigurationByClientName("SBAC");
    }

}
