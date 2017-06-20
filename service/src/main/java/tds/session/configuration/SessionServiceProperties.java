/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

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
