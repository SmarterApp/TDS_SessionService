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
import tds.session.Extern;
import tds.session.services.ExternService;
import tds.session.web.resources.ExternResource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class ExternControllerTest {
    private ExternContoller controller;
    private ExternService externService;

    @Before
    public void setUp() {
        HttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        externService = mock(ExternService.class);
        controller = new ExternContoller(externService);
    }

    @After
    public void tearDown() {}

    @Test
    public void shouldGetExternByClientName() {
        Extern extern = new Extern.Builder()
            .withClientName("SBAC")
            .withEnvironment("Development")
            .build();
        when(externService.getExternByClientName("SBAC")).thenReturn(Optional.of(extern));

        ResponseEntity<ExternResource> resource = controller.getExternByClientName("SBAC");

        assertThat(resource.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test(expected = NotFoundException.class)
    public void shouldHandleNotFoundExternByClientName() {
        when(externService.getExternByClientName("SBAC")).thenReturn(Optional.empty());
        controller.getExternByClientName("SBAC");
    }

}
