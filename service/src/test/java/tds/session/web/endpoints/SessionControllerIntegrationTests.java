package tds.session.web.endpoints;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import tds.session.SessionServiceApplication;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SessionServiceApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:8080")
public class SessionControllerIntegrationTests {
    @Test
    public void shouldReturnSession() {
        String sessionId = "06485031-B2B6-4CED-A0C1-B294EDA54DB2".toLowerCase();
        given()
            .accept(ContentType.JSON)
        .when()
            .get(String.format("/session/%s", sessionId))
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("session.id", equalTo(sessionId))
            .body("session.status", equalTo("closed"));
    }

    @Test
    public void shouldReturnNotFound() {
        String sessionId = "55585031-B2B6-4CED-A0C1-B294EDA54DB2";
        given()
            .accept(ContentType.JSON)
        .when()
            .get(String.format("/session/%s", sessionId))
        .then()
            .statusCode(404);
    }

    @Test
    public void shouldHandleNonUUID() {
        given().accept(ContentType.JSON).when().get(String.format("/session/invalidUUID")).then().statusCode(400);
    }
}