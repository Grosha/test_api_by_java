import api.APIHelper;
import api.EndPoints;
import org.junit.jupiter.api.*;
import pojo.car.Car;

import java.util.Random;

import static api.APIHelper.assertResponseMessage;
import static api.ResponseMessages.ERROR_CAR_ABSENT_IN_THE_LIST;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUpdateCar extends TestBaseAPI {
    private Car newCar = null;

    @BeforeEach
    void setUp() {

        String model = "TestModel" + new Random().nextInt(10000);
        newCar = new Car(model, "TestName", 1, "TestType");

        APIHelper.requestAddNewCar(newCar);
    }

    @AfterEach
    void tearDown() {
        APIHelper.requestDeleteCarModel(newCar.getModel());
    }

    @Test
    void testUpdateCarName() {
        String newCarName = "NewNameCarGrom";

        Car updatedCar = APIHelper.requestGetUpdatedCarWith("name", newCarName, newCar);
        assertEquals(updatedCar.getName(), newCarName, "Car name was not updated");
    }

    @Test
    void testUpdateCarModel() {
        String newCarModel = "NewModelCarGrom";

        Car updatedCar = APIHelper.requestGetUpdatedCarWith("model", newCarModel, newCar);
        assertEquals(updatedCar.getModel(), newCarModel, "Car model was not updated");
    }

    @Test
    void testUpdateCarType() {
        String newCarType = "NewTypeCarGromero";

        Car updatedCar = APIHelper.requestGetUpdatedCarWith("type", newCarType, newCar);
        assertEquals(updatedCar.getType(), newCarType, "Car type was not updated");
    }

    @Test
    void testUpdateCarTypeForNoneExistCar() {
        given()
                .param("type", "NewType")
                .when()
                .patch(EndPoints.updateCar, "NoExistCar")
                .then()
                .spec(assertResponseMessage(equalTo(ERROR_CAR_ABSENT_IN_THE_LIST)));
    }

    @Test
    void testUpdateNoneExistParameterCar() {
        Car updatedCar = APIHelper.requestGetUpdatedCarWith("test", "test", newCar);
        assertEquals(updatedCar, newCar, "Problem with negative cases for updating car");
    }

    @Test
    void testUpdateCarWithoutUpdateInfo() {
        Car updatedCar = APIHelper.requestUpdateCarWithoutParameters(newCar);
        assertEquals(updatedCar, newCar, "Problem with negative cases for updating car");
    }
}
