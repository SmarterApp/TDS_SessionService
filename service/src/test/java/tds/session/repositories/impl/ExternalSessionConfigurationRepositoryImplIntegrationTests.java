package tds.session.repositories.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

import tds.common.data.mysql.UuidAdapter;
import tds.session.ExternalSessionConfiguration;
import tds.session.repositories.ExternalSessionConfigurationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ExternalSessionConfigurationRepositoryImplIntegrationTests {
    @Autowired
    private ExternalSessionConfigurationRepository externalSessionConfigurationRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        String externsInsertSQL = "INSERT INTO session._externs (clientname, environment, shiftwindowstart, shiftwindowend, shiftformstart, shiftformend) VALUES ('SESSION-SBAC', 'Development', 1, 2, 10, 11);";
        String clientExternsInsertSQL = "INSERT INTO configs.client_externs VALUES (:uuid,'MultiClient_RTS_2013','MultiClient_RTS_2013','itembank',1,1,'RTS','RTS',1,'session',1,1,'SESSION-SBAC',1,'SESSION-SBAC',NULL,'Development',0,0,NULL,100000,1,NULL,NULL);";

        jdbcTemplate.update(externsInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(clientExternsInsertSQL, new MapSqlParameterSource("uuid", UuidAdapter.getBytesFromUUID(UUID.randomUUID())));
    }

    @Test
    public void shouldFindExternalSessionConfigurationByClientName() {
        Optional<ExternalSessionConfiguration> maybeExternalSessionConfiguration = externalSessionConfigurationRepository.findExternalSessionConfigurationByClientName("SESSION-SBAC");
        assertThat(maybeExternalSessionConfiguration).isPresent();

        ExternalSessionConfiguration config = maybeExternalSessionConfiguration.get();
        assertThat(config.getClientName()).isEqualTo("SESSION-SBAC");
        assertThat(config.getEnvironment()).isEqualTo("Development");
        assertThat(config.getShiftWindowStart()).isEqualTo(1);
        assertThat(config.getShiftWindowEnd()).isEqualTo(2);
        assertThat(config.getShiftFormStart()).isEqualTo(10);
        assertThat(config.getShiftFormEnd()).isEqualTo(11);

    }

    @Test
    public void shouldHandleWhenExternCannotBeFoundByClientName() {
        Optional<ExternalSessionConfiguration> maybeExternalSessionConfiguration = externalSessionConfigurationRepository.findExternalSessionConfigurationByClientName("FAKE");
        assertThat(maybeExternalSessionConfiguration).isNotPresent();
    }
}
