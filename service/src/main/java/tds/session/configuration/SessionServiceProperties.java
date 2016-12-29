package tds.session.configuration;

import com.google.common.base.Preconditions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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

    public void setExamUrl(String examUrl) {
        this.examUrl = removeTrailingSlash(Preconditions.checkNotNull(examUrl, "examUrl cannot be null"));
    }

    /**
     * If there is a trailing slash at the end of a supplied URL, remove it.
     *
     * @param url The URL being trimmed
     * @return The url without a trailing slash
     */
    private String removeTrailingSlash(String url) {
        return url.endsWith("/")
            ? url.substring(0, url.length() - 1)
            : url;
    }
}
