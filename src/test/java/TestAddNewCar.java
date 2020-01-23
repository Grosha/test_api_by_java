import api.EndPoints;
import api.ResponseMessages;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pojo.car.Car;
import pojo.car.Message;

import java.util.Random;
import java.util.stream.Stream;

import static helpers.APIHelper.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.startsWithIgnoringCase;

public class TestAddNewCar extends TestBaseAPI {

    @Test
    void testAddNewCar() {

        String model = "Accord_v" + new Random().nextInt(10000);
        Car newCar = new Car(model, "Honda", 1, "Sedan");

        given()
                .spec(requestSpecWithBody(new Gson().toJson(newCar)))
                .when()
                .post(EndPoints.car)
                .then()
                .spec(responseSpecEqualsMessage(ResponseMessages.NEW_CAR_ADDED));
    }

    @Test
    void testAddPresentedCar() {

        Car newCar = new Car("Accord", "Honda", 1, "Sedan");

        given()
                .spec(requestSpecWithBody(new Gson().toJson(newCar)))
                .when()
                .post(EndPoints.car)
                .then()
                .log().body()
                .statusCode(200)
                .body("message", startsWithIgnoringCase("Car presence in the list:"));
    }

    private static Stream<Arguments> carObjects() {
        return Stream.of(
                Arguments.of("{}", "Incorrect parameter 'model' in json body for object Car"),
                Arguments.of("{\"sd\":\"Accord\", \"model\":\"Hondas\", \"type\":\"Sedan\", \"status\":1}", "Incorrect parameter 'name' in json body for object Car"),
                Arguments.of("{\"name\":\"Accord\", \"models\":\"Hondas\", \"type\":\"Sedan\", \"status\":1}", "Incorrect parameter 'model' in json body for object Car"),
                Arguments.of("{\"name\":\"Accord\", \"model\":\"Hondas\"}", "Incorrect parameter 'type' in json body for object Car"),
                Arguments.of("{\"name\":\"Accord\", \"model\":\"Hondas\", \"type\":\"Sedan\", \"statas\":1}", "Incorrect parameter 'status' in json body for object Car"),
                Arguments.of("{\"name\":\"Accord\", \"model\":\"Hondas\", \"type\":\"Sedan\"}", "Incorrect parameter 'status' in json body for object Car"),
                Arguments.of("", "Problem with json Expecting value: line 1 column 1 (char 0)")
        );
    }

    @ParameterizedTest
    @MethodSource("carObjects")
    void testAddCarNegativeCases(String car, String message) {

        given()
                .spec(requestSpecWithBody(car))
                .when()
                .post(EndPoints.car)
                .then()
                .spec(responseSpecEqualsMessage(message));
    }

    @Test
    void testSmokeSuite() {

        Car newCar = new Car("Huracan", "Lamborghiniv", 1, "Sportcar");

        // add new car
        given()
                .spec(requestSpecWithBody(new Gson().toJson(newCar)))
                .when()
                .post(EndPoints.car)
                .then()
                .spec(responseSpecEqualsMessage(ResponseMessages.NEW_CAR_ADDED));

        // get just added car
        Car car = given()
                .param("model", newCar.getModel())
                .when()
                .get(EndPoints.car)
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();

        Assertions.assertEquals(newCar, car, "Information about just added car not equals information, which was sent");

        // update car
        String newModel = "Test" + newCar.getModel();
        Car updatedCar = given()
                .param("model", newModel)
                .when()
                .patch(EndPoints.updateCar, newCar.getModel())
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();

        Assertions.assertEquals(updatedCar.getModel(), newModel, "Information about just added car not equals information, which was sent");

        // remove just added/updated car
        String deleteCarMessage = String.format("Car model %s removed", newModel);
        given()
                .when()
                .delete(EndPoints.deleteCar, newModel)
                .then()
                .spec(responseSpecEqualsMessage(deleteCarMessage));


        // get removed car
        String getAbsentCarMessage = String.format("Car model %s is absent in the list", newModel);
        given()
                .param("model", newModel)
                .when()
                .get(EndPoints.car)
                .then()
                .spec(responseSpecEqualsMessage(getAbsentCarMessage));
    }
}
