package tds.session.web.endpoints;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import tds.session.SessionServiceApplication;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SessionServiceApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:8080")
@Transactional
public class SessionControllerIntegrationTests {
    @Test
    public void shouldReturnSessionWhenFoundById() {
        String sessionId = "06485031-B2B6-4CED-A0C1-B294EDA54DB2".toLowerCase();
        given()
            .accept(ContentType.JSON)
        .when()
            .get(String.format("/sessions/%s", sessionId))
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("session.id", equalTo(sessionId))
            .body("session.status", equalTo("closed"));
    }

    @Test
    public void shouldReturnNotFoundWhenSessionCannotBeFoundById() {
        String sessionId = "55585031-B2B6-4CED-A0C1-B294EDA54DB2";
        given()
            .accept(ContentType.JSON)
        .when()
            .get(String.format("/sessions/%s", sessionId))
        .then()
            .statusCode(404);
    }

    @Test
    public void shouldHandleNonUUIDWhenFindingSessionById() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(String.format("/sessions/invalidUUID"))
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldPauseASession() {
        String sessionId = "C7F1D37F-8A4A-4AD9-BF7A-915068C9D40D".toLowerCase();
        String newStatus = "ctrl_test";
        String selfRel =
                given()
                    .accept(ContentType.JSON)
                    .body(newStatus)
                .when()
                    .put(String.format("/sessions/%s/pause", sessionId))
                .then()
                    .statusCode(200)
                    .header("Location", equalTo(String.format("http://localhost:8080/sessions/%s", sessionId)))
                    .body("status", equalTo(newStatus))
                    .body("clientname", isEmptyOrNullString())  // Ensure that internal/extra data is not explosed to client
                    .body("session", isEmptyOrNullString())
                .extract()
                    .header("Location");


        // Verify the update happened
        given()
            .accept(ContentType.JSON)
        .when()
            .get(selfRel)
        .then()
            .statusCode(200)
            .body("session.id", equalTo(sessionId))
            .body("session.status", equalTo(newStatus));
    }
}
