import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AuthTest {
    static String login = DataGeneration.login();
    static String pass = DataGeneration.password();

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void setUpAll() {

        given()
                .spec(requestSpec)
                .body(new RegistrationDto("Vasya", "password", "active"))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    void testWhenLoginIsEmpty() {

        given()
                .spec(requestSpec)
                .body(new RegistrationDto(null, "password", "active"))
                .when()
                .post("/api/system/users/")
                .then()
                .statusCode(500);
    }


    @Test
    void testWhenPasswordIsEmpty() {

        given()
                .spec(requestSpec)
                .body(new RegistrationDto("vasya", null, "active"))
                .when()
                .post("/api/system/users/")
                .then()
                .statusCode(500);
    }


    @Test
    void testWhenStatusIsBlocked() {

        given()
                .spec(requestSpec)
                .body(new RegistrationDto("Katya", "password", "blocked"))
                .when()
                .post("/api/system/users/")
                .then()
                .statusCode(200);
    }

    @Test
    void testWhenStatusIsBlocked_Login() {

        given()
                .spec(requestSpec)
                .body(new Put("Katya", "password"))
                .when()
                .post("http://localhost:9999/api/auth")
                .then()
                .statusCode(400);
    }

    @Test
    void testVasyaLogin() {

        given()
                .spec(requestSpec)
                .body(new Put("Vasya", "password"))
                .when()
                .post("http://localhost:9999/api/auth")
                .then()
                .statusCode(200);
    }

    @Test
    void testVasyaLoginWithWrongPassword() {

        given()
                .spec(requestSpec)
                .body(new Put("Vasya", "12345"))
                .when()
                .post("http://localhost:9999/api/auth")
                .then()
                .statusCode(400);
    }

    @Test
    void testUnregisteredUser() {

        given()
                .spec(requestSpec)
                .body(new Put("AhalaiMahalaiPostGet", "password"))
                .when()
                .post("http://localhost:9999/api/auth")
                .then()
                .statusCode(400);
    }
}
