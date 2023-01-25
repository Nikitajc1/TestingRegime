import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGeneration {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    DataGeneration() {
    }

    private static void sendRequest(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users/")
                .then()
                .statusCode(200);
    }

    public static String generatedLogin() {
        return faker.name().firstName();
    }

    public static String generatedPassword() {
        return faker.name().lastName();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {

            return new RegistrationDto(
                    generatedLogin(),
                    generatedPassword(),
                    status
            );
        }

        public static RegistrationDto getRegisteredUser(String status) {
            RegistrationDto registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Value
    public static class RegistrationDto {

        String login;
        String password;
        String status;
    }
}
