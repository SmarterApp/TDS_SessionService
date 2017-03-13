package tds.session.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static tds.common.util.Preconditions.checkNotNull;

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
        this.examUrl = removeTrailingSlash(examUrl);
    }

    private static String removeTrailingSlash(String url) {
        return checkNotNull(url, "url cannot be null").endsWith("/")
            ? url.substring(0, url.length() - 1)
            : url;
    }
}
