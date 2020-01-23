import api.EndPoints;
import api.ResponseMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pojo.car.Car;
import pojo.car.Message;

import java.util.stream.Stream;

import static api.APIHelper.assertResponseMessage;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class TestGetCar extends TestBaseAPI {

    private static Stream<Arguments> carModels() {
        return Stream.of(
                Arguments.of("Accord", "{model=Accord, name=Honda, status=1, type=Sedan}"),
                Arguments.of("No car", "Car model No car is absent in the list")
        );
    }

    @ParameterizedTest
    @MethodSource("carModels")
    void testGetCarByModel(String modelName, String message) {
        given()
                .param("model", modelName)
                .when()
                .get(EndPoints.car)
                .then()
                .spec(assertResponseMessage(equalTo(message)));
    }

    private static Stream<Arguments> carParameters() {
        return Stream.of(
                Arguments.of("name", "Honda", ResponseMessages.ERROR_INCORRECT_PARAMETER),
                Arguments.of("status", "1", ResponseMessages.ERROR_INCORRECT_PARAMETER),
                Arguments.of("type", "Sedan", ResponseMessages.ERROR_INCORRECT_PARAMETER),
                Arguments.of("test", "test", ResponseMessages.ERROR_INCORRECT_PARAMETER)
        );
    }

    @ParameterizedTest
    @MethodSource("carParameters")
    void testGetCarByAnyParameters(String parameterName, String parameterValue, String message) {
        given()
                .param(parameterName, parameterValue)
                .when()
                .get(EndPoints.car)
                .then()
                .spec(assertResponseMessage(equalTo(message)));
    }

    @Test
    void testGetCar() {

        Car car = given()
                .when()
                .get(EndPoints.car)
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();

//        Assertions.assertNotEquals(car.getModel(), null, "Incorrect parameter model in car");
//        Assertions.assertNotEquals(car.getName(), null, "Incorrect parameter name in car");
//        Assertions.assertNotEquals(car.getType(), null, "Incorrect parameter type in car");
//        Assertions.assertNotEquals(car.getStatus(), null, "Incorrect parameter status in car");

        assertThat("Incorrect parameter model in car", car.getModel(), matchesPattern("\\w+"));
        assertThat("Incorrect parameter name in car", car.getName(), matchesPattern("\\w+"));
        assertThat("Incorrect parameter type in car", car.getType(), matchesPattern("\\w+"));
        assertThat("Incorrect parameter status in car", car.getStatus().toString(), matchesPattern("\\d+"));
    }

    @Test
    void testGetCarForFail() {

        Car car = given()
                .when()
                .get(EndPoints.car)
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();

        assertThat("Incorrect parameter model in car", car.getModel(), matchesPattern("\\d+"));
    }
}
