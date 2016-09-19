package tds.session.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import tds.session.Extern;
import tds.session.repositories.ExternRepository;
import tds.session.services.ExternService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExternServiceImplTest {
    private ExternRepository externRepository;
    private ExternService externService;

    @Before
    public void setUp() {
        externRepository = mock(ExternRepository.class);
        externService = new ExternServiceImpl(externRepository);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldGetExternByClientName() {
        Extern extern = new Extern("SBAC", "SIMULATION");
        when(externRepository.getExternByClientName("SBAC")).thenReturn(Optional.of(extern));
        Optional<Extern> optional = externService.getExternByClientName("SBAC");
        verify(externRepository).getExternByClientName("SBAC");

        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(extern);
    }
}
