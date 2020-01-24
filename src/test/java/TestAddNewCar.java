import api.APIHelper;
import api.EndPoints;
import api.ResponseMessages;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pojo.car.Car;

import java.util.Random;
import java.util.stream.Stream;

import static api.APIHelper.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAddNewCar extends TestBaseAPI {

    @Test
    void testAddNewCar() {

        String model = "Accord_v" + new Random().nextInt(10000);
        Car newCar = new Car(model, "Honda", 1, "Sedan");

        APIHelper.requestAddNewCar(newCar);
    }

    @Test
    void testAddPresentedCar() {

        Car newCar = new Car("Accord", "Honda", 1, "Sedan");

        given()
                .spec(requestSpecWithBody(new Gson().toJson(newCar)))
                .when()
                .post(EndPoints.car)
                .then()
                .spec(assertResponseMessage(startsWith("Car presence in the list:")));
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
                .spec(assertResponseMessage(equalTo(message)));
    }

    @Test
    void testSmokeSuite() {
        Car newCar = new Car("Huracan", "Lamborghiniv", 1, "Sportcar");

        // add new car
        APIHelper.requestAddNewCar(newCar);

        // get just added car
        Car car = APIHelper.requestGetCar("model", newCar.getModel());
        assertEquals(newCar, car, "Information about just added car not equals information, which was sent");

        // update car
        String newModel = "Test" + newCar.getModel();
        Car updatedCar = APIHelper.requestGetUpdatedCarWith("model", newModel, newCar);
        assertEquals(updatedCar.getModel(), newModel, "Information about just added car not equals information, which was sent");

        // remove just added/updated car
        APIHelper.requestDeleteCarModel(newModel);

        // get removed car
        String getAbsentCarMessage = String.format("Car model %s is absent in the list", newModel);
        given()
                .param("model", newModel)
                .when()
                .get(EndPoints.car)
                .then()
                .spec(assertResponseMessage(equalTo(getAbsentCarMessage)));
    }
}
