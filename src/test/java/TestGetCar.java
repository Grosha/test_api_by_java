import api.APIHelper;
import api.EndPoints;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pojo.car.Car;

import java.util.stream.Stream;

import static api.APIHelper.assertResponseMessage;
import static api.ResponseMessages.ERROR_INCORRECT_PARAMETER;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
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
                .spec(assertResponseMessage(hasToString(message)));
    }

    private static Stream<Arguments> carParameters() {
        return Stream.of(
                Arguments.of("name", "Honda", ERROR_INCORRECT_PARAMETER),
                Arguments.of("status", "1", ERROR_INCORRECT_PARAMETER),
                Arguments.of("type", "Sedan", ERROR_INCORRECT_PARAMETER),
                Arguments.of("test", "test", ERROR_INCORRECT_PARAMETER)
        );
    }

    @ParameterizedTest
    @MethodSource("carParameters")
    void testGetCarByAnyParameters(String paramName, String paramValue, String message) {
        given()
                .param(paramName, paramValue)
                .when()
                .get(EndPoints.car)
                .then()
                .spec(assertResponseMessage(equalTo(message)));
    }

    @Test
    void testGetCar() {
        Car car = APIHelper.requestGetAnyCar();

        assertThat("Incorrect parameter model in car", car.getModel(), matchesPattern("^\\w+( \\w+)|\\w+"));
        assertThat("Incorrect parameter name in car", car.getName(), matchesPattern("\\w+"));
        assertThat("Incorrect parameter type in car", car.getType(), matchesPattern("\\w+"));
        assertThat("Incorrect parameter status in car", car.getStatus().toString(), matchesPattern("\\d+"));
    }

    @Test
    void testGetCarForFail() {
        Car car = APIHelper.requestGetAnyCar();

        assertThat("Incorrect parameter model in car", car.getModel(), matchesPattern("\\d+"));
    }
}
