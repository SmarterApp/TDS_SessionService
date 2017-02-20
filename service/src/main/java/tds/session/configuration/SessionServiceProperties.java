package tds.session.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import tds.common.web.utils.UrlUtils;

@Component
@ConfigurationProperties("session-service")
public class SessionServiceProperties {
    private String examUrl = "";

    /**
     * @return url for the exam microservice
     */
    public String getExamUrl() {
        return examUrl;
    }

    public void setExamUrl(final String examUrl) {
        this.examUrl = UrlUtils.removeTrailingSlash(examUrl);
    }
}
