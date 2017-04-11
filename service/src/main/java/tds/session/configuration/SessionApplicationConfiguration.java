package tds.session.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import tds.common.configuration.CacheConfiguration;
import tds.common.configuration.DataSourceConfiguration;
import tds.common.configuration.RedisClusterConfiguration;
import tds.common.configuration.RestTemplateConfiguration;
import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;

@Configuration
@Import({
    ExceptionAdvice.class,
    DataSourceConfiguration.class,
    RestTemplateConfiguration.class,
    RedisClusterConfiguration.class,
    CacheConfiguration.class,
    SecurityConfiguration.class
})
public class SessionApplicationConfiguration {
}
