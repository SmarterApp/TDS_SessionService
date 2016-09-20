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
public class ExternControllerIntegrationTests {
    @Test
    public void shouldReturnExtern() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(String.format("/sessions/externs/%s", "SBAC"))
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("extern.clientName", equalTo("SBAC"))
            .body("extern.environment", equalTo("SIMULATION"));
    }

    @Test
    public void shouldReturnNotFoundWhenExternCannotBeFoundByClientName() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(String.format("/sessions/externs/%s", "FAKE"))
        .then()
            .statusCode(404);
    }
}

