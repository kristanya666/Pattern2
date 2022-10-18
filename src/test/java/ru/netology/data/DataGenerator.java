package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import lombok.Value;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;




import java.util.Locale;


public class DataGenerator {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();



    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationDto user) {

        given()
                .spec(requestSpec)
                .body(new RegistrationDto(user.login, user.password, user.status))
        .when()
                .post("/api/system/users")
        .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        String login = faker.name().username().toLowerCase();
        return login;
    }

    public static String getRandomPassword() {
        String password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            RegistrationDto user = new RegistrationDto(getRandomLogin(),getRandomPassword(),status);
            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) {
            RegistrationDto registeredUser = getUser(status);
            sendRequest(registeredUser);
            // Послать запрос на регистрацию пользователя с помощью вызова sendRequest(registeredUser)
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
