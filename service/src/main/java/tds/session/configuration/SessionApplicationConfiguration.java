package tds.session.configuration;

import org.springframework.context.annotation.Import;

import tds.common.web.advice.ExceptionAdvice;

@Import({ExceptionAdvice.class})
public class SessionApplicationConfiguration {
}
