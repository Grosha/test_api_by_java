import api.EndPoints;
import api.ResponseMessages;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import pojo.car.Car;
import pojo.car.Message;

import java.util.Random;

import static api.APIHelper.requestSpecWithBody;
import static api.APIHelper.assertResponseMessage;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestUpdateCar extends TestBaseAPI {
    private Car newCar = null;

    @BeforeEach
    void setUp() {

        String model = "TestModel" + new Random().nextInt(10000);
        newCar = new Car(model, "TestName", 1, "TestType");

        given()
                .spec(requestSpecWithBody(new Gson().toJson(newCar)))
                .when()
                .post(EndPoints.car)
                .then()
                .spec(assertResponseMessage(equalTo(ResponseMessages.NEW_CAR_ADDED)));
    }

    @AfterEach
    void tearDown() {

        given()
                .when()
                .delete(EndPoints.deleteCar, newCar.getModel())
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    void testUpdateCarName() {
        String newCarName = "NewNameCarGrom";

        Car updatedCar = given()
                .param("name", newCarName)
                .when()
                .patch(EndPoints.updateCar, newCar.getModel())
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();

//        Car updatedCar = APIHelper.updateCarWith("name", newCarName, newCar);

        Assertions.assertEquals(updatedCar.getName(), newCarName, "Car name was not updated");
    }

    @Test
    void testUpdateCarModel() {
        String newCarModel = "NewModelCarGrom";

        Car updatedCar = given()
                .param("model", newCarModel)
                .when()
                .patch(EndPoints.updateCar, newCar.getModel())
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();

        Assertions.assertEquals(updatedCar.getModel(), newCarModel, "Car model was not updated");
    }

    @Test
    void testUpdateCarType() {
        String newCarType = "NewTypeCarGromero";

        Car updatedCar = given()
                .param("type", newCarType)
                .when()
                .patch(EndPoints.updateCar, newCar.getModel())
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();

        Assertions.assertEquals(updatedCar.getType(), newCarType, "Car type was not updated");
    }

    @Test
    void testUpdateCarTypeForNoneExistCar() {
        given()
                .param("type", "NewType")
                .when()
                .patch(EndPoints.updateCar, "NoExistCar")
                .then()
                .spec(assertResponseMessage(equalTo(ResponseMessages.ERROR_CAR_ABSENT_IN_THE_LIST)));
    }

    @Test
    void testUpdateNoneExistParameterCar() {
        Car updatedCar = given()
                .param("test", "test")
                .when()
                .patch(EndPoints.updateCar, newCar.getModel())
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();

        Assertions.assertEquals(updatedCar, newCar, "Problem with negative cases for updating car");
    }

    @Test
    void testUpdateCarWithoutUpdateInfo() {
        Car updatedCar = given()
                .when()
                .patch(EndPoints.updateCar, newCar.getModel())
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();

        Assertions.assertEquals(updatedCar, newCar, "Problem with negative cases for updating car");
    }
}
