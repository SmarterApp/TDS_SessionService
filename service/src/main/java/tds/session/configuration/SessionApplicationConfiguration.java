package tds.session.configuration;

import org.springframework.context.annotation.Import;

import tds.common.configuration.DataSourceConfiguration;
import tds.common.web.advice.ExceptionAdvice;

@Import({ExceptionAdvice.class, DataSourceConfiguration.class})
public class SessionApplicationConfiguration {
}
