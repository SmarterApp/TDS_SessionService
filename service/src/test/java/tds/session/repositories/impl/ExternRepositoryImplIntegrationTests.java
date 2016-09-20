package tds.session.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import tds.session.Extern;
import tds.session.repositories.ExternRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExternRepositoryImplIntegrationTests {
    @Autowired
    private ExternRepository externRepository;

    @Test
    public void shouldFindExternByClientName() {
        Optional<Extern> externOptional = externRepository.getExternByClientName("SBAC");
        assertThat(externOptional).isPresent();
        assertThat(externOptional.get().getClientName()).isEqualTo("SBAC");
        assertThat(externOptional.get().getEnvironment()).isEqualTo("SIMULATION");
    }

    @Test
    public void shouldHandleWhenExternCannotBeFoundByClientName() {
        Optional<Extern> externOptional = externRepository.getExternByClientName("FAKE");
        assertThat(externOptional).isNotPresent();
    }
}
